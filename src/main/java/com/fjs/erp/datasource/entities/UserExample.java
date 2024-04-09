package com.fjs.erp.datasource.entities;

import java.util.ArrayList;
import java.util.List;


public class UserExample {

    protected List<Criteria> oredCriteria;


    //封装查询条件，以便在构建查询时使用
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }
    //GeneratedCriteria是一个抽象静态内部类，它作为一个基类，
    // 包含了具体的条件判断方法，如等于（equal）、不等于（notEqual）、
    // 大于（greaterThan）等。这些方法在GeneratedCriteria的子类中实现，
    // 用于构建具体的查询条件。
    protected abstract static class GeneratedCriteria {

    }

    public UserExample() {
        oredCriteria = new ArrayList<>();
    }
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }
}
