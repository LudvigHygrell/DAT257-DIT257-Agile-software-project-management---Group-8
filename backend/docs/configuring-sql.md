# SQL Configuration

To set up the initial database, there is a [setup script](../src/main/sql/create) that will set everything up
for you.

There are a few prerequisites to be able to run the setup:

1. You are working on a UNIX system with access to bash.
2. You have access to a passphrase file used to decrypt the super SQL user password.
3. You have `sudo` access and [`PostgresSQL`](https://www.postgresql.org/) installed on your machine.

Access to the passphrase is available for all team members, but must not be included
on the remote repository.

To gain access to the key, contact git user [`JaarmaCo`](https://github.com/JaarmaCo) and you will have it transferred to you.

During pre-release, the database super user password will be the `sha256` sum of the chorus of
Never gonna give you up by Rick Astley. More specifically, the string:

```
Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you
```


## Specifications

The database super user password (encrypted) is located in the file:
> `${git-root}/backend/src/main/sql/dbpasswords/benesphere.key.gpg`

The passphrase file should be placed at either of these locations:
> `$HOME/.benesphere_db/.secret/benesphere-pw.txt`
> `${git-root}/backend/.benesphere_db/.secret/benesphere-pw.txt`

Once the setup script has run, it should also give you an optional login script for each user at:
> `${git-root}/backend/.benesphere_db/login.sh` (regular user)
> `${git-root}/backend/.benesphere_db/login-root.sh` (super user)

Run these using `bash <path-to-login-script>`

