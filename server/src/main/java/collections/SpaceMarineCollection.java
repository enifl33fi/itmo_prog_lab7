package collections;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dataBases.DataBasesManager;
import dataBases.SpaceMarineTable;
import dataBases.UsersTable;
import element.AstartesCategory;
import element.CollectionPart;

public class SpaceMarineCollection implements InteractiveCollection {

    private final SpaceMarineTable spaceMarineTable;
    private final LinkedList<CollectionPart> data;
    private final java.time.LocalDate initDate;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * Constructs a new SpaceMarineCollection object. And set the initialization date.
     */
    public SpaceMarineCollection(DataBasesManager dbManager) {
        this.spaceMarineTable = dbManager.getSpaceMarineTable();
        this.data = this.spaceMarineTable.getSpaceMarineCollection();
        this.initDate = java.time.LocalDate.now();
    }
    @Override
    public String info() {
        readWriteLock.readLock().lock();
        try {
            StringBuilder answer = new StringBuilder();
            answer.append("Initialization date: ").append(this.initDate).append("\n");
            answer.append("Type of collection: ").append(this.data.getClass().getName()).append("\n");
            answer.append("Length of current collection: ").append(this.data.size()).append("\n");
            return answer.toString();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void add(CollectionPart elem) throws SQLException {
        readWriteLock.writeLock().lock();
        try {
            long newId = this.spaceMarineTable.addSpaceMarine(elem);
            elem.setId(newId);
            this.data.add(elem);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public String show() {
        readWriteLock.readLock().lock();
        try {
            StringBuilder answer = new StringBuilder();
            this.data.stream().forEach(answer::append);
            return answer.toString();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public String update(long id, CollectionPart elem, String owner) throws SQLException {
        readWriteLock.readLock().lock();
        CollectionPart curElem;
        try{
            if (this.data.size() == 0) {
                return "Collection is empty";
            }
            Stream<CollectionPart> subStream = this.data.stream().filter((CollectionPart currElem) -> currElem.getId() == id);
            curElem = subStream.reduce((a, b) -> b).orElse(null);
            if (curElem == null){
                return "No such id.";
            } else if (!curElem.getOwner().equals(owner)) {
                return "Access denied. You are not the owner of element with given id";
            }
        }finally {
            readWriteLock.readLock().unlock();
        }
        this.spaceMarineTable.update(id, owner, elem);
        this.set(curElem, elem);
        return String.format("Element with id %d updated.%n", id);
    }

    @Override
    public String removeById(long id, String owner) throws SQLException {
        CollectionPart curElem;
        readWriteLock.readLock().lock();
        try {
            if (this.data.size() == 0) {
                return "Collection is empty";
            }
            Stream<CollectionPart> subStream = this.data.stream().filter((CollectionPart currElem) -> currElem.getId() == id);
            curElem = subStream.reduce((a, b) -> b).orElse(null);
            if (curElem == null) {
                return "No such id.";
            } else if (!curElem.getOwner().equals(owner)) {
                return "Access denied. You are not the owner of element with given id";
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
        readWriteLock.writeLock().lock();
        try{
            this.spaceMarineTable.removeById(owner, id);
            this.data.remove(curElem);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return String.format("Element with id %d deleted.%n", id);
    }

    @Override
    public void clear(String owner) throws SQLException {
        readWriteLock.writeLock().lock();
        Predicate<CollectionPart> isOwned = (CollectionPart elem) -> {
            return elem.getOwner().equals(owner);
        };
        try {
            this.spaceMarineTable.clear(owner);
            this.data.removeIf(isOwned);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public String removeFirst(String owner) throws SQLException {
        String answer = null;
        readWriteLock.writeLock().lock();
        try {
            CollectionPart elem = this.data.stream()
                    .filter((CollectionPart curElem) -> curElem.getOwner().equals(owner))
                    .findFirst()
                    .orElse(null);
            if (elem == null){
                answer = "You don't have element's in this collection";
            } else {
                this.spaceMarineTable.removeFirst(owner);
                this.data.remove(elem);
                answer = "First element successfully removed";
            }
        } catch (NoSuchElementException e) {
            answer = "Collection is empty";
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return answer;
    }

    @Override
    public String head() {
        readWriteLock.readLock().lock();
        try {
            return this.data.getFirst().toString();
        } catch (NoSuchElementException e) {
            return "Collection is empty";
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public String removeLower(CollectionPart elem, String owner) throws SQLException {
        StringBuilder answer = new StringBuilder();
        Iterator<CollectionPart> iterator = this.data.iterator();
        CollectionPart curElem;
        readWriteLock.writeLock().lock();
        try{
            while (iterator.hasNext()) {
                curElem = iterator.next();
                if (elem.compareTo(curElem) > 0 && curElem.getOwner().equals(owner)) {
                    this.spaceMarineTable.removeById(curElem.getOwner(), curElem.getId());
                    answer.append(String.format("Element with id %d deleted.%n", curElem.getId()));
                    iterator.remove();
                }
            }
            if (answer.length() == 0){
                answer.append("Where is no elements lower than this.").append("\n");
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }

        return answer.toString();
    }

    @Override
    public int countByCategory(AstartesCategory category) {
        readWriteLock.readLock().lock();
        try{
            return (int) this.data.stream()
                    .filter((CollectionPart curElem) -> Objects.equals(curElem.getCategory(), category))
                    .count();
        }finally {
            readWriteLock.readLock().unlock();
        }


    }

    @Override
    public String filterContainsName(String namePart) {
        readWriteLock.readLock().lock();
        try {
            StringBuilder answer = new StringBuilder();
            this.data.stream()
                    .filter((CollectionPart curElem) -> curElem.getName().contains(namePart))
                    .forEach(answer::append);

            if (answer.length() == 0) {
                answer.append("Where is no such elements").append("\n");
            }
            return answer.toString();
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public String printFieldAscendingHeartCount() {
        readWriteLock.readLock().lock();
        try {
            StringBuilder answer = new StringBuilder();
            int sum = this.data.stream()
                    .map(CollectionPart::getHeartCount)
                    .reduce(0, Integer::sum);
            System.out.println(sum);

            this.data.stream()
                    .map(CollectionPart::getHeartCount)
                    .sorted()
                    .forEach(heartCount -> answer.append(heartCount).append("\n"));
            if (answer.length() == 0){
                answer.append("Collection is empty").append("\n");
            }
            return answer.toString();
        }finally {
            readWriteLock.readLock().unlock();
        }

    }

    @Override
    public List<String> toListCSV() {
        List<String> strList = new ArrayList<>();
        for (CollectionPart curElem : this.data) {
            strList.add(curElem.toLineCSV());
        }
        return strList;
    }

    @Override
    public void set(CollectionPart updElem, CollectionPart elem) {
        this.readWriteLock.writeLock().lock();
        try{
            updElem.setName(elem.getName());
            updElem.setCoordinates(elem.getCoordinates());
            updElem.setHealth(elem.getHealth());
            updElem.setHeartCount(elem.getHeartCount());
            updElem.setCategory(elem.getCategory());
            updElem.setMeleeWeapon(elem.getMeleeWeapon());
            updElem.setChapter(elem.getChapter());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

}