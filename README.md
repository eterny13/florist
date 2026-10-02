# florist

## ローカルDBを使った実行・Integration Test

MySQLを起動すると、Flywayがテーブル作成と初期データ投入を行う

```bash
docker compose -f etc/docker/docker-compose-mysql.yml up -d --wait
./gradlew integrationTest
```

`flower`、`bouquet`、`bouquet_flower`には、花10種類と花束5種類の初期データが登録される

Integration Testは`src/integrationTest/groovy`または`src/integrationTest/java`に配置する
`integrationTest`実行時に`flywayMigrate`が先に実行されるため、テスト前に手動でSQLを流す必要はない

停止

```bash
docker compose -f etc/docker/docker-compose-mysql.yml down
```
