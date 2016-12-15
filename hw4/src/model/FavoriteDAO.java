/**
 * Author : Jialu Chen
 */

package model; 

import databeans.Favorite;

import org.genericdao.*;

public class FavoriteDAO extends GenericDAO<Favorite> {
    public FavoriteDAO(ConnectionPool cp) throws DAOException {
        super(Favorite.class, ConstantCopy.TABLE_FAVORITE, cp);
    }
    
    public void delete(int id, int ownerID) throws RollbackException {
        try {
            Transaction.begin();
            Favorite p = read(id);
            System.out.println("1");

            if (p == null) {
                throw new RollbackException("Link does not exist: id=" + id);
            }

            if (ownerID != p.getUserId()) {
            	System.out.println("2");
                throw new RollbackException("This link not owned by this user");    
            }

            delete(id);
            Transaction.commit();
        } finally {
            if (Transaction.isActive()) Transaction.rollback();
        }
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
