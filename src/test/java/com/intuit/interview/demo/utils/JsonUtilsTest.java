package com.intuit.interview.demo.utils;

import com.intuit.interview.demo.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JsonUtilsTest {

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private JsonUtils jsonUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void convertToObject_returnsObjectWhenValidJson() throws Exception {
        String json = "{\"name\":\"test\"}";
        TestClass expectedObject = new TestClass();
        expectedObject.setName("test");
        when(mapper.readValue(json, TestClass.class)).thenReturn(expectedObject);

        TestClass result = jsonUtils.covertToObject(json, TestClass.class);

        assertEquals(expectedObject, result);
    }

    @Test
    public void getJsonFromObject_returnsJsonWhenValidObject() throws Exception {
        TestClass object = new TestClass();
        object.setName("test");
        String expectedJson = "{\"name\":\"test\"}";
        when(mapper.writeValueAsString(object)).thenReturn(expectedJson);

        String result = jsonUtils.getJsonFromObject(object);
        assertEquals(expectedJson, result);
    }

    private static class TestClass {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestClass testClass = (TestClass) o;
            return name.equals(testClass.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}