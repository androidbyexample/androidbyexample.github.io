package com.javadude.javasample;

import java.util.Objects;

interface OnClickListener {  // OBSERVER
    void onClick(Button button); // SINGLE ABSTRACT METHOD = SAM
}

class Button { // OBSERVABLE
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void ifIKnowThatIAmClicked() {
        // user touches inside me, and releases inside me
        onClickListener.onClick(this);
    }
}

class ButtonTest {
    static class ClickListener1 implements OnClickListener {
        @Override
        public void onClick(Button button) {
            System.out.println("Clicked!!!");
        }
    }
    public static void main(String[] args) {
        Button button1 = new Button();
        button1.setOnClickListener(new ClickListener1());
        int x = 42;
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Button button) {
                System.out.println(x);
                System.out.println("Anonymous Clicked!!!");
            }});
        button1.setOnClickListener(
                button -> {
                    System.out.println("Anonymous Clicked!!!");
                    System.out.println("Anonymous Clicked!!!");
                    System.out.println("Anonymous Clicked!!!");
                    System.out.println("Anonymous Clicked!!!");
                }
        );
    }
}


class Person {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}


public class JavaSample {
}
