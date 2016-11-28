package dao; 
/**
 * Author : Jialu Chen
 * date: 11/27/2016
 * course: 08672
 */

import databean.Favorite;
import org.genericdao.*;

public class FavoriteDAO extends GenericDAO<Favorite> {
    public FavoriteDAO(ConnectionPool cp) throws DAOException {
        super(Favorite.class, ConstantCopy.TABLE_FAVORITE, cp);
    }


    public synchronized void increment(Favorite item) throws RollbackException {
        try {
            Transaction.begin();
            item.setClickCount(item.getClickCount() + 1);
            update(item);
            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public Favorite[] getFavoritesList(int userId) throws RollbackException {
        return match(MatchArg.equals("userId",userId));
    }

    public Favorite[] getFavoritesList() throws RollbackException {
        return match();
    }
}
