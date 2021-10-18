package br.com.luizfp.cursobranas.infra.repository.database;

import br.com.luizfp.cursobranas.domain.entity.Coupon;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class CouponRepositoryDatabase implements CouponRepository {
    @NotNull
    private final DatabaseConnectionAdapter databaseConnection;

    public CouponRepositoryDatabase(@NotNull final DatabaseConnectionAdapter databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @NotNull
    @Override
    public Optional<Coupon> getByCode(@NotNull final String couponCode) {
        final List<DatabaseResultRow> items =
                databaseConnection.query("select * from coupon c where c.code = ?", couponCode);
        final Optional<DatabaseResultRow> optional = items
                .stream()
                .filter(db -> db.get("code").equals(couponCode))
                .findFirst();
        if (optional.isPresent()) {
            final DatabaseResultRow db = optional.get();
            return Optional.of(new Coupon(db.get("id"),
                                          db.get("code"),
                                          db.get("expires_at"),
                                          db.get("percentage_discount")));
        } else {
            return Optional.empty();
        }
    }
}
