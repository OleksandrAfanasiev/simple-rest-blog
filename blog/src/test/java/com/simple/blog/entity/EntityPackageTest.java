package com.simple.blog.entity;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import com.simple.blog.entity.core.Role;
import org.junit.Test;

import java.util.List;

public class EntityPackageTest {

    private String packageName = Role.class.getPackage().getName();

    @Test
    public void validate() {
        List<PojoClass> pojoClasses = PojoClassFactory.getPojoClasses(packageName);

        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new SetterTester())
                .with(new GetterTester())
                .build();
        validator.validate(pojoClasses);
    }
}