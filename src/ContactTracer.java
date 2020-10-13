import java.util.*;

public class ContactTracer {

    private Map<String, Person> people;

    private final int TIME_TO_CONTAGIOUS = 60;

    private class Person {
        private String name;
        private Map<String, Contact> contacts;

        private Person(String name) {
            this.name = name;
            this.contacts = new HashMap<>();
        }

        private String getName() {
            return this.name;
        }

        private void addNewContact(Person otherPerson, int time) {
            if (this.contacts.containsKey(otherPerson.getName())) {
                this.contacts.get(otherPerson.getName()).addNewTime(time);
                return;
            }

            Contact toAdd = new Contact(this, otherPerson, time);
            this.contacts.put(otherPerson.getName(), toAdd);
            otherPerson.contacts.put(this.getName(), toAdd);
        }

        private Map<String, Contact> getAllContacts() {
            return this.contacts;
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
    }

    private class Contact{
        private Person person1;
        private Person person2;

        private int latestContactTime;
        private Set<Integer> contactTimes;

        private Contact(Person person1, Person person2, int time) {
            this.person1 = person1;
            this.person2 = person2;

            this.latestContactTime = time;

            this.contactTimes = new HashSet<>();
            this.contactTimes.add(time);
        }

        private int getLatestTime() {
            return this.latestContactTime;
        }

        private Set<Integer> getContactTimes() {
            return this.contactTimes;
        }

        private void addNewTime(int time) {
            this.contactTimes.add(time);
            if (time > this.latestContactTime) {
                this.latestContactTime = time;
            }
        }
    }

    /**
     * Initialises an empty ContactTracer with no populated contact traces.
     */
    public ContactTracer() {
        people = new HashMap<>();
    }

    /**
     * Initialises the ContactTracer and populates the internal data structures
     * with the given list of contract traces.
     * 
     * @param traces to populate with
     * @require traces != null
     */
    public ContactTracer(List<Trace> traces) {
        people = new HashMap<>();
        for (Trace trace : traces) {
            this.addTrace(trace);
        }
    }

    /**
     * Adds a new contact trace to 
     * 
     * If a contact trace involving the same two people at the exact same time is
     * already stored, do nothing.
     * 
     * @param trace to add
     * @require trace != null
     */
    public void addTrace(Trace trace) {
        Person person1 = this.addPersonIfAbsent(trace.getPerson1());
        Person person2 = this.addPersonIfAbsent(trace.getPerson2());

        person1.addNewContact(person2, trace.getTime());
    }

    private Person addPersonIfAbsent(String personName) {
        if (!this.people.containsKey(personName)) {
            this.people.put(personName, new Person(personName));
        }

        return this.people.get(personName);
    }

    /**
     * Gets a list of times that person1 and person2 have come into direct 
     * contact (as per the tracing data).
     *
     * If the two people haven't come into contact before, an empty list is returned.
     * 
     * Otherwise the list should be sorted in ascending order.
     * 
     * @param person1 
     * @param person2
     * @return a list of contact times, in ascending order.
     * @require person1 != null && person2 != null
     */
    public List<Integer> getContactTimes(String person1, String person2) {
        Person person = this.people.get(person1);
        if (person == null) {
            return new ArrayList<>();
        }

        Contact contactBetweenPeople = person.getAllContacts().get(person2);
        if (contactBetweenPeople == null) {
            return new ArrayList<>();
        }

        List<Integer> times = new ArrayList<>(contactBetweenPeople.getContactTimes());
        Collections.sort(times);
        return times;
    }

    /**
     * Gets all the people that the given person has been in direct contact with
     * over the entire history of the tracing dataset.
     * 
     * @param person to list direct contacts of
     * @return set of the person's direct contacts
     */
    public Set<String> getContacts(String person) {
        return this.people.get(person).getAllContacts().keySet();
    }

    /**
     * Gets all the people that the given person has been in direct contact with
     * at OR after the given timestamp (i.e. inclusive).
     * 
     * @param person to list direct contacts of
     * @param timestamp to filter contacts being at or after
     * @return set of the person's direct contacts at or after the timestamp
     */
    public Set<String> getContactsAfter(String person, int timestamp) {
        Set<String> contactsAfterTime = new HashSet<>();
        Person person1 = this.people.get(person);
        for (String person2 : person1.contacts.keySet()) {
            Contact contact = person1.contacts.get(person2);
            if (contact.getLatestTime() >= timestamp) {
                contactsAfterTime.add(person2);
            }
        }

        return contactsAfterTime;
    }

    /**
     * Initiates a contact trace starting with the given person, who
     * became contagious at timeOfContagion.
     * 
     * Note that the return set shouldn't include the original person the trace started from.
     * 
     * @param person to start contact tracing from
     * @param timeOfContagion the exact time person became contagious
     * @return set of people who may have contracted the disease, originating from person
     */
    public Set<String> contactTrace(String person, int timeOfContagion) {
        return this.contactTraceHelper(person, timeOfContagion, new HashSet<>(Collections.singletonList(person)));
    }

    /**
     * Implements a recursive DFS for the purpose of contact tracing after a certain time.
     *
     * @param personName to start contact tracing from
     * @param timeOfContagion the exact time person became contagious
     * @param checked set of people who have been infected already
     * @return set of people who may have contracted the disease, originating from personName
     */
    private Set<String> contactTraceHelper(String personName, int timeOfContagion, Set<String> checked) {
        Set<String> infectedContacts = new HashSet<>();
        for (String otherPersonName : this.getContactsAfter(personName, timeOfContagion)) {
            if (!checked.contains(otherPersonName)) {
                checked.add(otherPersonName);
                List<Integer> times = this.getContactTimes(personName, otherPersonName);
                infectedContacts.add(otherPersonName);
                infectedContacts.addAll(this.contactTraceHelper(
                        otherPersonName, times.get(0) + TIME_TO_CONTAGIOUS, checked));
            }
        }

        return infectedContacts;
    }
}
