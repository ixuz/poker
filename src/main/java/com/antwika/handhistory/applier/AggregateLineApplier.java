package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class AggregateLineApplier implements ILineApplier {
    private static final Logger logger = LoggerFactory.getLogger(AggregateLineApplier.class);
    final List<ILineApplier> lineAppliers;

    public AggregateLineApplier(List<ILineApplier> lineAppliers) {
        this.lineAppliers = lineAppliers;
    }

    @Override
    public boolean apply(TableData tableData, ILine line) {
        boolean applied = false;

        for (var lineApplier : lineAppliers) {
            applied = lineApplier.apply(tableData, line);
            if (applied) {
                break;
            }
        }

        if (!applied) {
            logger.warn("No suitable LineApplier for: '{}'", line);
        }

        return applied;
    }
}
