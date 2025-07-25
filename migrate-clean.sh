# Optional: Set env vars or use your .env
export DB_URL=jdbc:postgresql://localhost:5432/store
export DB_USER=gigz
export DB_PASS=241619

# Clean the DB
mvn flyway:clean \
  -Dflyway.url=$DB_URL \
  -Dflyway.user=$DB_USER \
  -Dflyway.password=$DB_PASS \
  -Dflyway.cleanDisabled=false
