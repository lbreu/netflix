### Design

I first started out with designing the database. I came up with a two table design where there would be a user table and a device table.
The user table holds the `id`, `pub_cred`, and `priv_cred`. 
The device table holds the `user_id`, `device_id`, `token`, and `status`.
I ended up not using status and opted for nullifying the user_id upon disassociation.
Obviously in a production system the `priv_cred` values would be encrypted, however for ease of testing and demonstration as well as for speed I chose not to include encryption in this exercise.
The device id is a unique device identifier that the the frontend or this system would come up with using mac address, guid, serial numbers, etc. Thus, we take it as a param.  

A user logs in using a public and private credential pair on a device. This is done by calling `user/auth` with publicCredential, privateCredential, and deviceId as params.
A given assumption was that users authenticate on an authenticated but unassociated device, so I did not need to insert a new device into the device table in the authentication call.
I represent an authenticated but unassociated device with the `device_id` and `token` being present, but the `user_id` set to `NULL`.
When a user authenticates on a particular device or associates the device with themselves, the `user_id` for the corresponding `device_id` is set to the user's id.
After a user is authenticated, they can use a token, returned in the `user/auth` call and `device/associate` call in the response entity to authenticate any further calls such as `user/logout`, `device/disassociate`, and `device/associate`.

`user/logout` takes a token as the param and will log the user out of that token's session by disassociating the device that the token is associated with from the user by setting the corresponding `user_id` to `NULL`.

`device/disassociate` works in a similar way, but takes a token and a device id as params and will disassociate the given device from the user as long as the device is already associated with the user.

`device/associate` takes in a token and device id as params and associates the given device with the authenticating user.

I included two other endpoints, `user/create` and `device/create` to help demonstrate the system's capabilities.
`user/create` takes publicCredential and privateCredential as params and `device/create` takes in deviceId as the param.

For the case of a "user-less device" such as a floor room model, they can authenticate with the pre-set public credential: "default" private credential: "password" pair.

All endpoints are POSTs as we are writing data to be processed.

I have included various sequence diagrams and other sketches to give an idea of how my design evolved.

### Architecture

See sequence diagrams in images folder

### Images
 
See images folder

