package com.softdesign.devintensive.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileDTO implements Parcelable {
    private String mFullName;
    private String mPhoto;
    private String mRating;
    private String mCodeLines;
    private String mProjects;
    private String mPhone;
    private String mEmail;
    private String mRepo;
    private String mVk;
    private String mBio;
    private String mAvatar;

    public ProfileDTO(Profile profileData) {
        mFullName = profileData.getFullName();
        mPhoto = profileData.getPhoto();
        mRating = String.valueOf(profileData.getRating());
        mCodeLines = String.valueOf(profileData.getCodeLines());
        mProjects = String.valueOf(profileData.getProjects());
        mPhone = profileData.getPhone();
        mEmail = profileData.getEmail();
        mRepo = profileData.getRepo();
        mVk = profileData.getVk();
        mBio = profileData.getBio();
        mAvatar = profileData.getAvatar();
    }

        protected ProfileDTO(Parcel in) {
            mFullName = in.readString();
            mPhoto = in.readString();
            mRating = in.readString();
            mCodeLines = in.readString();
            mProjects = in.readString();
            mPhone = in.readString();
            mEmail = in.readString();
            mRepo = in.readString();
            mVk = in.readString();
            mBio = in.readString();
            mAvatar = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mFullName);
            dest.writeString(mPhoto);
            dest.writeString(mRating);
            dest.writeString(mCodeLines);
            dest.writeString(mProjects);
            dest.writeString(mPhone);
            dest.writeString(mEmail);
            dest.writeString(mRepo);
            dest.writeString(mVk);
            dest.writeString(mBio);
            dest.writeString(mAvatar);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<ProfileDTO> CREATOR = new Parcelable.Creator<ProfileDTO>() {
            @Override
            public ProfileDTO createFromParcel(Parcel in) {
                return new ProfileDTO(in);
            }

            @Override
            public ProfileDTO[] newArray(int size) {
                return new ProfileDTO[size];
            }
        };


    public String getFullName() {
        return mFullName;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getRating() {
        return mRating;
    }

    public String getCodeLines() {
        return mCodeLines;
    }

    public String getProjects() {
        return mProjects;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getRepo() {
        return mRepo;
    }

    public String getVk() {
        return mVk;
    }

    public String getBio() {
        return mBio;
    }

    public String getAvatar() {
        return mAvatar;
    }
}