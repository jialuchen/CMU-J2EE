/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package model;

import databeans.Favorite;
import databeans.User;
import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class Model {
    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

    public Model(ServletConfig config) throws ServletException {
        try {
            String jdbcDriver = config.getInitParameter("jdbcDriverName");
            String jdbcURL = config.getInitParameter("jdbcURL");

            ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);
            userDAO = new UserDAO(pool);
            favoriteDAO = new FavoriteDAO(pool);

            try {
                if (userDAO.getCount() == 0) {

                    insertUserWithFavoriteList(new User("jialuc@gmail.com", "Jialu", "Chen", "123"));
                    insertUserWithFavoriteList(new User("luluchen@gmail.com", "Lu", "Lu", "123"));
                    insertUserWithFavoriteList(new User("RenSheng@gmail.com", "Ren", "Sheng", "123"));
                    insertUserWithFavoriteList(new User("HaoYun@gmail.com", "Hao", "Yun", "123"));
                }


            } catch (RollbackException e) {
                e.printStackTrace();
            }
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }

    private void insertUserWithFavoriteList(User user) throws RollbackException {
        userDAO.create(user);
        favoriteDAO.create(new Favorite(user.getUserId(), 0, "www.airbnb.com", "Airbnb"));
        favoriteDAO.create(new Favorite(user.getUserId(), 0, "www.trip.com", "Trip"));
        favoriteDAO.create(new Favorite(user.getUserId(), 0, "www.qq.com", "QQ"));
        favoriteDAO.create(new Favorite(user.getUserId(), 0, "www.wechat.com", "Wechat"));

    }

    public FavoriteDAO getFavoriteDAO() {
        return favoriteDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
    
}

