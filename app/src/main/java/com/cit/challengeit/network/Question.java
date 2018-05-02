package com.cit.challengeit.network;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Muzaffer YILMAZ on 11.3.2018.
 */

public class Question implements Serializable {

    public String id;
    public String a;
    public String b;
    public String c;
    public String d;
    public String correct;
    public String image;
    public String text;

    // client side fields
    public List<String> mChoiceOrder;

    public Question() {
        createChoiceOrder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private void createChoiceOrder() {
        mChoiceOrder = Arrays.asList("a", "b", "c", "d");
        Collections.shuffle(mChoiceOrder);
    }
}
