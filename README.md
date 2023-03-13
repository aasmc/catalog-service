# Catalog Service (Polar Book Shop)

Educational project based on the book Cloud Native Spring in Action by ThomasVitale.

GitHub repo with code examples from the book: https://github.com/ThomasVitale/cloud-native-spring-in-action

### Service responsibilities:
1. View the list of books in the catalog.
2. earch books by their International Standard Book Number (ISBN).
3. Add a new book to the catalog.
4. Edit information for an existing book.
5. Remove a book from the catalog.

### Endpoints:
1. /books GET:
   1. 200 returns BookCollection, gets all books in the catalog
2. /books POST:
   1. 201 returns Book, adds a new book to the collection, 
   2. 422 - a book with the given ISBN already exists.  
3. /books/{isbn} GET:
   1. 200 returns Book, gets the book with the given ISBN
   2. 404 no book with the given ISBN exists
4. /books/{isbn} PUT:
   1. 200 returns Book, updates the book with the given ISBN
   2. 201 returns Book, creates a book with the given ISBN
5. /books/{isbn} DELETE:
   1. 204 deletes the book with the given ISBN

