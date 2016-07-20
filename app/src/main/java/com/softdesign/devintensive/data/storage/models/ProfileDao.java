package com.softdesign.devintensive.data.storage.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PROFILE".
*/
public class ProfileDao extends AbstractDao<Profile, Long> {

    public static final String TABLENAME = "PROFILE";

    /**
     * Properties of entity Profile.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FullName = new Property(1, String.class, "fullName", false, "FULL_NAME");
        public final static Property Photo = new Property(2, String.class, "photo", false, "PHOTO");
        public final static Property Rating = new Property(3, int.class, "rating", false, "RATING");
        public final static Property CodeLines = new Property(4, int.class, "codeLines", false, "CODE_LINES");
        public final static Property Projects = new Property(5, int.class, "projects", false, "PROJECTS");
        public final static Property Phone = new Property(6, String.class, "phone", false, "PHONE");
        public final static Property Email = new Property(7, String.class, "email", false, "EMAIL");
        public final static Property Repo = new Property(8, String.class, "repo", false, "REPO");
        public final static Property Vk = new Property(9, String.class, "vk", false, "VK");
        public final static Property Bio = new Property(10, String.class, "bio", false, "BIO");
        public final static Property Avatar = new Property(11, String.class, "avatar", false, "AVATAR");
    };

    private DaoSession daoSession;


    public ProfileDao(DaoConfig config) {
        super(config);
    }
    
    public ProfileDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROFILE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"FULL_NAME\" TEXT NOT NULL UNIQUE ," + // 1: fullName
                "\"PHOTO\" TEXT," + // 2: photo
                "\"RATING\" INTEGER NOT NULL ," + // 3: rating
                "\"CODE_LINES\" INTEGER NOT NULL ," + // 4: codeLines
                "\"PROJECTS\" INTEGER NOT NULL ," + // 5: projects
                "\"PHONE\" TEXT," + // 6: phone
                "\"EMAIL\" TEXT," + // 7: email
                "\"REPO\" TEXT," + // 8: repo
                "\"VK\" TEXT," + // 9: vk
                "\"BIO\" TEXT," + // 10: bio
                "\"AVATAR\" TEXT);"); // 11: avatar
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROFILE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Profile entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFullName());
 
        String photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindString(3, photo);
        }
        stmt.bindLong(4, entity.getRating());
        stmt.bindLong(5, entity.getCodeLines());
        stmt.bindLong(6, entity.getProjects());
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(8, email);
        }
 
        String repo = entity.getRepo();
        if (repo != null) {
            stmt.bindString(9, repo);
        }
 
        String vk = entity.getVk();
        if (vk != null) {
            stmt.bindString(10, vk);
        }
 
        String bio = entity.getBio();
        if (bio != null) {
            stmt.bindString(11, bio);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(12, avatar);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Profile entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFullName());
 
        String photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindString(3, photo);
        }
        stmt.bindLong(4, entity.getRating());
        stmt.bindLong(5, entity.getCodeLines());
        stmt.bindLong(6, entity.getProjects());
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(8, email);
        }
 
        String repo = entity.getRepo();
        if (repo != null) {
            stmt.bindString(9, repo);
        }
 
        String vk = entity.getVk();
        if (vk != null) {
            stmt.bindString(10, vk);
        }
 
        String bio = entity.getBio();
        if (bio != null) {
            stmt.bindString(11, bio);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(12, avatar);
        }
    }

    @Override
    protected final void attachEntity(Profile entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Profile readEntity(Cursor cursor, int offset) {
        Profile entity = new Profile( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // fullName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // photo
            cursor.getInt(offset + 3), // rating
            cursor.getInt(offset + 4), // codeLines
            cursor.getInt(offset + 5), // projects
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // phone
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // email
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // repo
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // vk
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // bio
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // avatar
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Profile entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFullName(cursor.getString(offset + 1));
        entity.setPhoto(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRating(cursor.getInt(offset + 3));
        entity.setCodeLines(cursor.getInt(offset + 4));
        entity.setProjects(cursor.getInt(offset + 5));
        entity.setPhone(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEmail(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setRepo(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setVk(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setBio(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setAvatar(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Profile entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Profile entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}