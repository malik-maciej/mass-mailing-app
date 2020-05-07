## Wdrożenie Microsoft Azure
Aplikacja została wdrożona jako App Service z użyciem Dockera.

Nie było możliwe wdrożenie bazy danych PostgreSQL z powodu ograniczeń subskrypcji studenckiej, więc baza danych została stworzona w pliku (H2).

Przy starcie aplikacji tworzony jest user - admin oraz baza 1000 adresów email, które zostały wygenerowane randomowo 
i zapisane do pliku, po czym są wczytywane i zapisywane do bazy danych.

Aplikacja umożliwia wysyłanie jednorazowo maili do wybranej ilości adresów. Adresy są randomowe, ponieważ nie ma systemu rejestracji.

Aplikację można swobodnie rozwinąć o system rejestracji, można rozszerzyć konta użytkowników, można dodać role moderatorów, 
zwykłych użytkowników i zarządzać ich dostępem. Można również dodać nowe funkcjonalności, jak wysyłanie maili na konkretne 
adresy, do wybranej grupy użytkowników, biorąc pod uwagę np. przedział wiekowy lub lokalizację. Można dodać możliwość subskrypcji 
danych treści. Można rozszerzyć dostępny formularz o dodatkowe opcje, które może zawierać mailing.

## Wykorzystane technologie
* Java 8
* Spring (Boot, Data, Security)
* Maven
* Lombok
* H2
* SendGrid
* HTML, Bootstrap, JavaScript
* JUnit, Mockito, AssertJ
* Docker
* Git

## Cennik
Wykorzystano warstwę cenową S1, która zawiera 1.75 GB pamięci. Miesięczny koszt tej warstwy cenowej wynosi 73.87 EUR miesięcznie, 
co w przeliczeniu rocznym wynosi około 886.44 EUR. Podany koszt uwzględnia lokalizację: Australia Środkowa. Wykorzystana 
warstwa pozwala na automatycznie skalowanie.

Serwer bazodanowy PostgreSQL w konfiguracji podstawowej kosztowałby 21.28 Euro za 1 rdzeń wirtualny oraz 0.08 Euro za 1 GB/miesiąc. 
W tej konfiguracji możemy mieć maksymalnie dwa rdzenie wirtualne. Magazyn możemy ustawić od 5 GB do 1024 GB, z zastrzeżeniem, 
nie może być skalowany w dół. Jest możliwość przechowywania kopii zapasowej od 7 do 35 dni. Przy tej opcji w konfiguracji 
2 rdzenie wirtualne i 50 GB pamięci magazynu, miesięcznie płacilibyśmy około 46.77 Euro.

SendGrid w opcji darmowej gwarantuje 25 000 maili miesięcznie. Kolejna opcja to <i>Bronze</i>, w której mamy do dyspozycji 
40 000 maili za 9.95 USD/msc. Następna z opcji <i>Silver</i> daje nam dwie dodatkowe opcje (Dedicated IP Address i Sub-user 
management) oraz 100 0000 maili miesięcznie, ale już za 79.95 USD.

## Pozytywne i negatywne aspekty aplikacji
#### Plusy:
* Prosta nawigacja.
* Bezpieczny system logowania.
* Pokrycie testami jednostkowymi.
* Przejrzysty kod, użycie Lomboka spowodowało zmniejszenie linii kodu.
* Zastosowanie największego frameworka dostępnego dla Javy - Spring, wykorzystano z niego między innymi Spring Security, 
który zapewnia uwierzytelnianie, autoryzację i inne funkcje bezpieczeństwa, dzięki czemu dodanie nowych funkcjonalności 
np. rejestracji jest ułatwione. Inny z modułów - Spring Data umożliwia wygodne operacje na danych z bazy.
#### Minusy:
* Brak wdrożenia bazy danych PostgreSQL.
* Brak systemu rejestracji.
* Ograniczona funkcjonalność, która może jednak zostać rozszerzona o dodatkowe opcje.
* Brak paczkowania maili, przy większej ilości wysyłanej jednocześnie rośnie czas odpowiedzi, które jest niedopuszczalne, 
konieczne jest zwracanie cząstkowych statusów. Aktualnie użytkownik nie wie ile maili wysłał i ile to jeszcze potrwa. 
Dostaje dopiero informację po wszystkim.

## Enpointy
Dostęp do aplikacji dostępny jest przy podstawowej autoryzacji - podanie username i password. Natomiast wysyłanie maili wymaga roli admina.

<b>/login</b> oraz <b>/logout</b> obsługuje Spring Security i nie są mapowane w aplikacji.
* Formularz wysyłania maili dostępny jest pod:<br>
<b>nazwaaplikacji.azurewebsites.net/</b>;<br>
metoda: GET;<br>
w razie braku autoryzacji przekierowuje na /login.

* Wysyłanie mailingu dostępne jest pod:<br>
<b>nazwaaplikacji.azurewebsites.net/api</b>;<br>
metoda: POST;<br>
body (JSON): { "subject": "tytuł maila", "content": "treść maila", "count": 1 };<br>
zwracany jest status: SUCCESS lub FAILURE.

## Testy
Testy zostały wykonane przy użyciu <b>jmeter</b>.

Raport dostępny jest w folderze <b>/docs/jmeter/index.html</b>. 
Według niego i oceny APDEX request na endpoint GET / jest celujący (0.9), ze średnim czasem odpowiedzi 0.36 sekundy. 
POST na /api, przy wysyłaniu jednego maila na raz został oceniony jako dostateczny z oceną 0.5 i średnim czasem odpowiedzi 
0.65 sekundy. Wraz z zwiększaniem ilości maili rośnie diametralnie też czas odpowiedzi, wchodząc na niedopuszczalny poziom. 
Przy 10 wysłanych mailach, czas odpowiedzi przekraczał 3 sekundy. Przy 100 mailach na raz response time wynosił około 25 sekund. 
Ostatnią przetestowaną ilością było 300 maili, a czas odpowiedzi urósł do minuty. Aplikacja umożliwia wysyłanie do 1000 
wiadomości na raz, a na odpowiedź trzeba by pewnie czekać kilka minut, co mogłoby zostać odebrane jako brak działania, ponieważ
w tym czasie nie wiadomo czy zostały wysłane maile i w jakiej ilości oraz nie wiadomo ile to jeszcze potrwa.

Ogólny APDEX wyniósł 0.83, który jest dobrym wynikiem, jednak wynik ten jest głównie zasługą jednego endpointu.
Średni czas odpowiedzi wynosił około 0.8 sekundy.