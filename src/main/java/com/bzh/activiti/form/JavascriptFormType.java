package com.bzh.activiti.form;

import org.activiti.engine.form.AbstractFormType;

public class JavascriptFormType extends AbstractFormType {

    @Override
    public String getName() {
        return "javascript";
    }

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        return propertyValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        return (String) modelValue;
    }
}
