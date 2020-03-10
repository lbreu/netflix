## Schema

#### USER

|    Key    |    Value     |
|-----------|-------------:|
| id        | bigint       |
| pub_cred  | varchar(200) |
| priv_cred | varchar(200) |


#### DEVICE

|    Key    |    Value     |
|-----------|-------------:|
| device_id | bigint       |
| user_id   | bigint       |
| status    | enum(I,A)    |
| token     | varchar(200) |

