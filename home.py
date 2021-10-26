from random import randint

#bien player

player = input()
computer = randint(0,2)

if computer == 0:
    computer = "Dam"
elif computer == 1:
    computer = "La"
elif computer == 2:
    computer = "Keo"

print("============================")
print("Computer chose: " + computer)
print("Player chose: " + player)
print("============================")

if player == computer:
    print("Draw")
else:
    if player == "Keo" and computer == "Dam":
            print("Player Lose")
    elif player == "Keo" and computer == "La":
            print("Player Win")
    
    elif player == "Dam" and computer == "Keo":
            print("Player Win") 
    elif player == "Dam" and computer == "La":
            print("Player Lose")

    elif player == "La" and computer == "Keo":
            print("Player Lose")
    elif player == "La" and computer == "Dam":
            print("Player Win")
    elif player != "Dam" and "La" and "Keo":
            print("Chon sai, hay cho lai")
