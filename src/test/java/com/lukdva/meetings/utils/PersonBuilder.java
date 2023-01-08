//package com.lukdva.meetings.utils;
//
//import com.lukdva.meetings.models.Person;
//
//public class PersonBuilder {
//    private String id = TestUtils.getRandomUUID();
//    private String name = "John Brooks";
//
//    public static PersonBuilder aPerson() {
//        return new PersonBuilder();
//    }
//
//    public PersonBuilder withId(String id) {
//        this.id = id;
//        return this;
//    }
//
//    public PersonBuilder withName(String name) {
//        this.name = name;
//        return this;
//    }
//
//    public Person build() {
//        return new Person(id, name);
//    }
//}
