
## APIs Supported

#### POST `http://localhost:8080/user/auth?publicCredential=<public_credential>&privateCredential=<private_credential>&deviceId=<deviceId>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Authenticates a user and device |  /user/auth  | publicCredential (the user's public credential e.g. a username), privateCredential (the user's private credential e.g. password, deviceId (the user's unique device identifier) | 


#### POST `http://localhost:8080/user/logout?token=<token>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Logs the user out and disassociates the device being used to log out |  /user/logout | token (user's token) | 

#### POST `http://localhost:8080/device/associate?token=<token>&deviceId<device_id>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Associates a device given the device id with the authenticating user |  /device/associate | token (user's token), deviceId (device id to associate with the user) | 


#### POST `http://localhost:8080/device/disassociate?token=<token>&deviceId=<device_id>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Disassociates a device given the device id with the authenticating user  |  /device/disassociate | token (user's token), deviceId (device id to disassociate with the user) | 


#### POST `http://localhost:8080/user/create?publicCredential=<public_credential>&privateCredential=<private_credential>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Creates a user with the given public and private credentials |  /user/create | publicCredential (the user's public credential e.g. a username), privateCredential (the user's private credential e.g. password)  | 


#### POST `http://localhost:8080/device/create?deviceId=<deviceId>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Creates a device with the given device id |  /device/create | deviceId (the unique device identifier) | 

#### GET `http://localhost:8080/user/get?token=<token>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Returns the authenticated user's user id and public credential |  /user/get | token (user's token) | 

#### POST `http://localhost:8080/device/get?token=<token>`
| Description   |      Path      |  Query Params |
|---------------|:--------------:|--------------:|
| Returns all the devices associated with the authenticated user |  /device/get | token (user's token) | 

