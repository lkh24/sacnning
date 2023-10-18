# sacnning: QR Code Scanning Authentication

## Overview

**sacnning** is an authentication system that leverages QR codes for secure and efficient user logins. Through the generation of unique QR codes that have a finite lifespan, this system ensures a seamless connection between the backend and frontend, allowing for swift user identification and validation.

## Process Breakdown:

1. **QR Code Generation**: The backend creates a unique, random string that serves as the content of the QR code.
2. **Server-Side Storage**: This generated string is saved on the server, accompanied by an expiration timer ensuring its transient nature.
3. **QR Code Creation**: Leveraging third-party libraries, the system generates a QR code embedding the unique string.
4. **Frontend Interaction**: The frontend showcases this QR code and periodically sends requests to the backend to ascertain if the code has been scanned.
5. **User Authentication**: Once the user scans the code, the backend associates this action with the user's profile and notifies the frontend of a successful login.

## Module Analysis:

### User Module:

The core of any authentication system, this module discerns the individual scanning the code, ensuring that each scan is tied to a specific user profile.

### Activity Module:

To better structure the life cycle of each QR code, the activity module was conceptualized. It dictates:

1. The inception and termination of a QR code's validity.
2. Storage and management of generated QR codes.

## Technologies Incorporated:

- **QR Code Generation**: Efficiently create QR codes that are easily readable and uniquely identifiable.
- **Image Handling**: Ensure optimal image uploads, keeping QR clarity intact.
- **Timers**: Oversee the life span of each QR code, preventing any unauthorized access post-expiration.
- **Random String Creation**: Generate unique strings, ensuring each QR code's distinctiveness.
- **Cache Management**: Temporarily store and manage the unique strings, facilitating rapid matching and verification.

