package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.core.querycondition.LogicalCondition;
import io.jmix.core.querycondition.PropertyCondition;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MenuItemFilterHelperTest {

    @Test
    public void testFilterItems() {
        //TODO implement
    }

    @Test
    public void testFindPropertyCondition() {
        var helper = new MenuItemFilterHelper();
        var conditions = IntStream.range(0, 3)
                .mapToObj(i -> PropertyCondition
                        .create("p" + i, "", "")
                        .skipNullOrEmpty()
                )
                .collect(Collectors.toList());
        var rootCondition = LogicalCondition.and(
                LogicalCondition.and(conditions.get(0), conditions.get(1)),
                conditions.get(2)
        );
        assertNull(helper.findPropertyCondition(rootCondition, "p1"));
        assertNull(helper.findPropertyCondition(rootCondition, "p2"));
        assertNull(helper.findPropertyCondition(rootCondition, "p3"));

        conditions.forEach(c -> c.setParameterValue("v"));
        assertEquals(conditions.get(1), helper.findPropertyCondition(rootCondition, "p1"));
        assertEquals(conditions.get(2), helper.findPropertyCondition(rootCondition, "p2"));
        assertNull(helper.findPropertyCondition(rootCondition, "p3"));
    }

    @Test
    public void testCreateConditionStream() {
        var helper = new MenuItemFilterHelper();
        var conditions = IntStream.range(0, 3)
                .mapToObj(i -> PropertyCondition.create("p" + i, "", ""))
                .collect(Collectors.toList());
        var rootCondition = LogicalCondition.and(
                LogicalCondition.and(conditions.get(0), conditions.get(1)),
                conditions.get(2)
        );
        assertEquals(
                conditions,
                helper.createConditionStream(rootCondition)
                        .filter(c -> c instanceof PropertyCondition)
                        .collect(Collectors.toList())
        );
    }


}