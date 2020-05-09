# bw_rs-backup
[![Actions Status](https://github.com/kamko/bw_rs-backup/workflows/docker%20build/badge.svg)](https://github.com/kamko/bw_rs-backup/actions "docker build status badge")
[![image metadata](https://images.microbadger.com/badges/image/kamko/bw_rs-backup.svg)](https://microbadger.com/images/kamko/bw_rs-backup "kamko/bw_rs-backup image metadata")

- Backups database, attachments and icon cache used by [bitwarden_rs](https://github.com/dani-garcia/bitwarden_rs) Bitwarden server implementation.
- Backups are collected into an encrypted ZIP and send off to [Backblaze B2](https://www.backblaze.com/b2/cloud-storage.html).
- On error it retries the backup in 60 seconds and sends a notification to Telegram group chat.

## How to run
```
docker run \
    -e <all-config-keys> \
    -v <bitwarden_rs_data_folder>:/bw-data \
    kamko/bw_rs-backup
```

## Configuration
```
BACKUP_CRON=<quartz-cron-expression>
BACKUP_ZIP_PASSWORD=<zip-password>
BACKUP_B2_KEY_ID=<b2-app-key-id>
BACKUP_B2_KEY_VALUE=<b2-app-key>
BACKUP_B2_BUCKET_ID=<b2-bucket-id>
BACKUP_TELEGRAM_TOKEN=<telegram-bot-token>
BACKUP_TELEGRAM_CHAT_ID=<telegram-chat-id>
```

## Docker hub
[kamko/bw_rs-backup](https://hub.docker.com/r/kamko/bw_rs-backup)

## License
[Apache-2.0](LICENSE)
