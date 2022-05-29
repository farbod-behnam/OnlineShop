package com.OnlineShop.enums;

public enum CategoryEnum
{
    digital_devices,
    video_games,
    clothes;

    @Override
    public String toString()
    {
        return name().replaceAll("_", " ").trim().strip();
    }
}
