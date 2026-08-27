# Asteroid-Threat-Detection-System
An asteroid detection system that uses Nasa's near earth object api to pull asteroid data and locations.

!!If you are using off my github ensure your api key is stored inside a .env and matches from Nasa's api website
!!Make sure java jdk and python is up to date

This program uses FastAPI to collect asteroid data such as location, size, name, and if it is a threat.
The data is pipelined into a Java backend which handles formatting and displaying results via swing gui.
