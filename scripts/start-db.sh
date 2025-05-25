#!/bin/bash
echo "Starting PostgreSQL database..."
docker-compose up -d postgres

echo "Waiting for PostgreSQL to be ready..."
until docker-compose exec postgres pg_isready -U companyuser; do
  sleep 2
done

echo "Database is ready!"
echo "You can access:"
echo "  - PostgreSQL at localhost:5432"
echo "  - Adminer at http://localhost:8090 (run: docker-compose --profile tools up -d)"
echo "  - pgAdmin at http://localhost:5050 (run: docker-compose --profile tools up -d)"