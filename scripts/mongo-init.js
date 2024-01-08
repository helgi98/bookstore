db.createUser({
    user: 'bookstore_user',
    pwd: 'password',
    roles: [
        {
            role: 'readWrite',
            db: 'bookstore_service',
        },
    ],
});