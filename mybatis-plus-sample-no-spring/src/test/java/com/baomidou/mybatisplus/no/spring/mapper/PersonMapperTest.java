package com.baomidou.mybatisplus.no.spring.mapper;

import com.baomidou.mybatisplus.no.spring.entity.Person;
import org.junit.Test;

/**
 * PersonMapper Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>12/20/2020</pre>
 */
public class PersonMapperTest extends MapperBaseTest<PersonMapper> {

    private PersonMapper mapper = initMapper();


    @Override
    protected String getSqlFile() {
        return "/db/schema-h2.sql";
    }

    @Test
    public void test() {
        System.out.println(mapper);
        Person person = new Person().setName("老李");
        mapper.insert(person);
        System.out.println("结果: " + mapper.selectById(person.getId()));
    }
}
