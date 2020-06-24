#!/bin/bash
echo "Client start"

sleep 1

dateTime="2020-07-05T11:00:00Z"
printf "\nClient gets Movie with Screenings by dateTime: %s\n" "$dateTime"
curl -v "http://localhost:8080/movie/list/$dateTime" | jq

sleep 3

screeningId=3
printf "\n\nClient gets Screening by id: %s\n" "$screeningId"
curl -v "http://localhost:8080/screening/$screeningId" | jq

sleep 3

clientName="Lorem"
clientSurname="Ipsum-ŁóźżćńśŚĘÓŁŃĆŹŻĄ"
kindOfTicketId=1
seatsIds="8,9"
printf "\n\nClient makes reservation with name: %s, surname: %s, kindOfTicketId: %s and seatsIds: %s\n" "$clientName" "$clientSurname" "$kindOfTicketId" "$seatsIds"
curl -v http://localhost:8080/reservation -d "name=$clientName&surname=$clientSurname&kindOfTicketId=$kindOfTicketId&screeningId=$screeningId&seatsIds=$seatsIds" | jq

sleep 1

echo "Client done!"
