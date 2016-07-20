package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.res.UserModelRes;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "PROFILE")
public class Profile {
    @Id
    private Long id;

    @NotNull
    @Unique
    private String fullName;

    private String photo;

    private int rating;

    private int codeLines;

    private int projects;

    private String phone;

    private String email;

    private String repo;

    private String vk;

    private String bio;

    private String avatar;

    /** Used for active entity operations. */
    @Generated(hash = 89320040)
    private transient ProfileDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Profile(UserModelRes.User user) {
        this.fullName = user.getFullName();
        this.photo = user.getPublicInfo().getPhoto();
        this.rating = user.getProfileValues().getRating();
        this.codeLines = user.getProfileValues().getLinesCode();
        this.projects = user.getProfileValues().getProjects();
        this.phone = user.getContacts().getPhone();
        this.email = user.getContacts().getEmail();
        this.repo = user.getRepositories().getRepo().get(0).getGit();
        this.vk = user.getContacts().getVk();
        this.bio = user.getPublicInfo().getBio();
        this.avatar = user.getPublicInfo().getAvatar();
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1351849779)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProfileDao() : null;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getVk() {
        return this.vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public String getRepo() {
        return this.repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getProjects() {
        return this.projects;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public int getCodeLines() {
        return this.codeLines;
    }

    public void setCodeLines(int codeLines) {
        this.codeLines = codeLines;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Generated(hash = 1253032481)
    public Profile(Long id, @NotNull String fullName, String photo, int rating,
            int codeLines, int projects, String phone, String email, String repo, String vk,
            String bio, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.photo = photo;
        this.rating = rating;
        this.codeLines = codeLines;
        this.projects = projects;
        this.phone = phone;
        this.email = email;
        this.repo = repo;
        this.vk = vk;
        this.bio = bio;
        this.avatar = avatar;
    }

    @Generated(hash = 782787822)
    public Profile() {
    }
}
