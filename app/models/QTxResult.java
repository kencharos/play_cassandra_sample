package models;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTxResult is a Querydsl query type for TxResult
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTxResult extends EntityPathBase<TxResult> {

    private static final long serialVersionUID = 186766053L;

    public static final QTxResult txResult = new QTxResult("txResult");

    public final BooleanPath applied = createBoolean("applied");

    public QTxResult(String variable) {
        super(TxResult.class, forVariable(variable));
    }

    public QTxResult(Path<? extends TxResult> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTxResult(PathMetadata<?> metadata) {
        super(TxResult.class, metadata);
    }

}

