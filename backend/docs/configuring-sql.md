# SQL Configuration

To set up the initial database, there is a [setup script](../src/main/sql/create) that will set everything up
for you.

There are a few prerequisites to be able to run the setup:

1. You are working on a UNIX system with access to bash.
2. You have `sudo` access and [`PostgresSQL`](https://www.postgresql.org/) installed on your machine.

There is also an additional optional prerequisite that for when a secure password selection
for the root user is required:

3. A passphrase file used to decrypt the local password file of the repository.

> **_NOTE_**: You may need to `cd` into the `backend/src/main/sql` directory.

If all you are doing is setting up, or resetting the database, you can simply run `cd backend/src/main/sql && ./create`.
The script will then prompt for a bunch of options. These are there for when the application eventually contains database
passwords that need protection. For now, you can simply just enter `y` for any `y/n` prompts and enter nothing for text
fields. Or you may run `create --quick-setup` instead of `create` in which case, all prompts are skipped.

> **_IMPORTANT_**: Ensure the script has LF line endings and not CRLF. This can be an issue if cloned the repository from
> Windows, or if you opened the file in VsCode on Windows. If the script won't run, this may be why. The solution is to
> either explicitly open the file in VsCode, set the ending type to LF (option to the right in the banner at the bottom
> of the VsCode window) and run again. You may also change the line endings using the `dos2unix` command.

## Details about password protection

Access to the passphrase is available for all team members, but must not be included
on the remote repository.

To gain access to the passphrase for the key, contact git user [`JaarmaCo`](https://github.com/JaarmaCo) and you will have 
it transferred to you.

During pre-release, the secure database super user password will be the `sha256` sum of the chorus of
Never gonna give you up by Rick Astley. More specifically, the string:

```
Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you
```

The default password for the database super user is the sha256 sum of the setup script.

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

