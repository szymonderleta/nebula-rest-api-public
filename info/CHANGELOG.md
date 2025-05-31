# Nebula Rest Api Changelog

## v 3.1.0 Release
- Added handling for expired token exceptions. Now endpoints return 401 Unauthorized status with body when any controller method using tokenProvider.isValid encounters an expired token:
  ```json
  {
    "message": "ACCESS_TOKEN_EXPIRED",
    "error": "TOKEN_EXPIRED",
    "timestamp": "2025-05-20T18:14:57.202066872"
  }
  ```

## v 3.0.0 Public Release
- Added over 400 unit and integration tests.
- Added an endpoint to refresh the accessToken (using the cookie with a valid refreshToken). Removed the old JWT token cookie system and replaced it with both accessToken and refreshToken, which are now stored as HttpOnly cookies. The accessToken is valid for 1 hour, while the refreshToken is valid for over 20 days.
- Fixed small errors and refactored some parts of the code.

## v 2.0.0 Beta Release
This version is intended for testing purposes in a pre-production environment.

## v1.0.0
- Alpha Release  
