/*
 * Stream-Pi - Free & Open-Source Modular Cross-Platform Programmable Macro Pad
 * Copyright (C) 2019-2021  Debayan Sutradhar (rnayabed),  Samuel Quiñones (SamuelQuinones)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

/*
ServerInfo.java

Stores basic information about the server - name, platform type

Contributors: Debayan Sutradhar (@rnayabed)
 */

package com.stream_pi.client.info;

import com.gluonhq.attach.orientation.OrientationService;
import com.gluonhq.attach.storage.StorageService;
import com.stream_pi.util.exception.MinorException;
import com.stream_pi.util.exception.SevereException;
import com.stream_pi.util.platform.Platform;
import com.stream_pi.util.platform.ReleaseStatus;
import com.stream_pi.util.version.Version;
import javafx.geometry.Orientation;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientInfo
{
    private final Version version;
    private final ReleaseStatus releaseStatus;
    private final Platform platform;
    private String prePath;
    private final Version minPluginSupportVersion;
    private final Version communicationProtocolVersion;
    private String buildDate;
    private String license;
    private Orientation orientation = null;

    private static ClientInfo instance = null;

    private ClientInfo()
    {
        version = new Version(1,0,0);
        minPluginSupportVersion = new Version(1,0,0);
        communicationProtocolVersion = new Version(1,0,0);

        releaseStatus = ReleaseStatus.EA;

        String osName = System.getProperty("os.name").toLowerCase();

        prePath = System.getProperty("user.home")+"/Stream-Pi/Client/";

        if(osName.contains("windows"))
        {
            platform = Platform.WINDOWS;
        }
        else if (osName.contains("linux"))
        {
            platform = Platform.LINUX;
        }
        else if(osName.contains("android") || osName.contains("ios"))
        {
            StorageService.create().ifPresent(s-> s.getPrivateStorage().ifPresentOrElse(sp-> prePath = sp.getAbsolutePath()+"/Stream-Pi/Client/",
                    ()-> prePath = null));

            platform = Platform.valueOf(osName.toUpperCase());
        }
        else if (osName.contains("mac"))
        {
            platform = Platform.MAC;
        }
        else
        {
            platform = Platform.UNKNOWN;
        }


        if (isPhone())
        {
            OrientationService.create().ifPresent(orientationService ->
            {
                if(orientationService.getOrientation().isPresent())
                {
                    orientationService.orientationProperty().addListener((observableValue, oldOrientation, newOrientation) ->
                    {
                        if (oldOrientation != newOrientation)
                        {
                            orientation = newOrientation;
                        }
                    });
                }
            });
        }



        try
        {
            InputStream inputStream = ClientInfo.class.getResourceAsStream("build.properties");
            if (inputStream != null)
            {
                Properties properties = new Properties();
                properties.load(inputStream);
                inputStream.close();

                buildDate = properties.getProperty("build.date");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unable to fetch build.properties!", e);
        }

        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClientInfo.class.getResourceAsStream("LICENSE"))));

            StringBuilder licenseTxt = new StringBuilder();
            while(true)
            {
                String line = bufferedReader.readLine();

                if(line == null)
                {
                    break;
                }

                licenseTxt.append(line).append("\n");
            }

            license = licenseTxt.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unable to fetch LICENSE!", e);
        }

    }

    public static synchronized ClientInfo getInstance()
    {
        if(instance == null)
        {
            instance = new ClientInfo();
        }

        return instance;
    }

    public String getPrePath()
    {
        return prePath;
    }

    public Platform getPlatform()
    {
        return platform;
    }

    public Version getVersion() {
        return version;
    }

    public ReleaseStatus getReleaseStatus()
    {
        return releaseStatus;
    }

    public Version getMinPluginSupportVersion()
    {
        return minPluginSupportVersion;
    }

    public Version getCommunicationProtocolVersion()
    {
        return communicationProtocolVersion;
    }

    public boolean isPhone()
    {
        return getPlatform() == Platform.ANDROID || getPlatform() == Platform.IOS;
    }

    public String getBuildDate()
    {
        return buildDate;
    }

    public String getLicense()
    {
        return license;
    }

    public Orientation getOrientation()
    {
        return orientation;
    }
}
