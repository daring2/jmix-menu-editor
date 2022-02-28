package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.core.LoadContext;
import io.jmix.core.querycondition.Condition;
import io.jmix.core.querycondition.LogicalCondition;
import io.jmix.core.querycondition.PropertyCondition;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Component
public class MenuItemFilterHelper {

    public List<MenuItemEntity> filterItems(List<MenuItemEntity> items, LoadContext<MenuItemEntity> loadContext) {
        if (items.isEmpty())
            return items;
        var captionCondition = findPropertyCondition(
                loadContext.getQuery().getCondition(), "caption"
        );
        if (captionCondition == null)
            return items;
        var captionExp = "" + captionCondition.getParameterValue();
        var itemSet = new HashSet<MenuItemEntity>();
        itemSet.add(items.get(0));
        for (var item : items) {
            if (!containsIgnoreCase(item.getCaption(), captionExp))
                continue;
            while (item != null) {
                itemSet.add(item);
                item = item.getParent();
            }
        }
        return items.stream().filter(itemSet::contains)
                .collect(Collectors.toList());
    }


    public PropertyCondition findPropertyCondition(Condition condition, String property) {
        condition = condition != null ? condition.actualize(emptySet()) : null;
        return createConditionStream(condition)
                .filter(c -> c instanceof PropertyCondition)
                .map(c -> (PropertyCondition) c)
                .filter(c -> property.equals(c.getProperty()))
                .findFirst().orElse(null);
    }

    public Stream<Condition> createConditionStream(Condition condition) {
        if (condition instanceof LogicalCondition) {
            return ((LogicalCondition) condition)
                    .getConditions().stream()
                    .flatMap(this::createConditionStream);
        } else {
            return Stream.ofNullable(condition);
        }
    }

}
