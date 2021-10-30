package br.com.luizfp.cursobranas.infra.repository.database;

import br.com.luizfp.cursobranas.domain.entity.Coupon;
import br.com.luizfp.cursobranas.domain.entity.CouponNotFoundException;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class CouponRepositoryDatabase implements CouponRepository {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public CouponRepositoryDatabase(@NotNull final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @NotNull
    @Override
    public Coupon getByCode(@NotNull final String couponCode) {
        final Optional<DatabaseResultRow> optional =
                databaseConnection.maybeOne("select * from coupon c where c.code = ? and active", couponCode);
        if (optional.isPresent()) {
            final DatabaseResultRow db = optional.get();
            return new Coupon(db.get("id"),
                              db.get("code"),
                              db.get("expires_at"),
                              db.get("percentage_discount"));
        } else {
            throw new CouponNotFoundException(couponCode);
        }
    }
}
