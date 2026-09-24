@echo off
curl -X POST "http://localhost:9200/_bulk?pretty" -H "Content-Type: application/x-ndjson" --data-binary @users_bulk.ndjson   