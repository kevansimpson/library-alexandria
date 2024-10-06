package com.aravo.library.data.repository;

import com.aravo.library.data.Persistable;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class OrphanRemover {
    private final JdbcTemplate template;

    @Autowired
    public OrphanRemover(JdbcTemplate jdbc) {
        template = jdbc;
    }

    public void removeAllOrphans(Work work, String table) {
        template.update(String.format("DELETE FROM %s WHERE work_id = ?", table), work.getId());
    }

    public <T extends Persistable> void removeOrphans(Work work, Set<T> children, String table) {
        removeOrphans(work, children, table, "id");
    }

    public <T extends Persistable> void removeOrphans(Work work, Set<T> children, String table, String idColumn) {
        switch (children.size()) {
            case 0 -> removeAllOrphans(work, table);
            case 1 -> template.update(
                    String.format("DELETE FROM %s WHERE work_id = ? AND %s != ?", table, idColumn),
                    work.getId(), children.stream().findFirst().get().getId());
            default -> {
                String idList = String.join(",", collectIds(children));
                template.update(String.format(
                        "DELETE FROM %s WHERE work_id = %d AND %s NOT IN (%s)", table, work.getId(), idColumn, idList));
            }
        }
    }

    protected <T extends Persistable> List<String> collectIds(Set<T> children) {
        return children.stream().map(c -> String.valueOf(c.getId())).toList();
    }
}
