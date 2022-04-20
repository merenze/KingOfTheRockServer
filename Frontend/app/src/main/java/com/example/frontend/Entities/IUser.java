package com.example.frontend.Entities;

public interface IUser {
    public String getUsername();

    public void setUsername(String username);

    public String getAuthToken();

    public void setAuthToken(String authToken);

    public boolean getIsAdmin();

    public void setIsAdmin(boolean isAdmin);

    public String toString();
}
