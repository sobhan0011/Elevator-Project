number_of_request = 8
apartment_floor = 200


# Function to perform C-LOOK on the request array starting from the given floor
def c_look(arr, reference_floor):
    seek_count = 0
    distance = 0
    current_floor = 0

    down = []
    up = []

    seek_sequence = []

    # floors under the current floor will be serviced once the elevator comes back to the beginning (bottom end, 0 floor)

    for i in range(number_of_request):
        if arr[i] < reference_floor:
            down.append(arr[i])
        if arr[i] > reference_floor:
            up.append(arr[i])

    # Sorting down and up request arrays
    down.sort()
    up.sort()

    # First service the requests on the up side of the current floor
    for i in range(len(up)):
        current_floor = up[i]

        # Appending current track
        # seek sequence
        seek_sequence.append(current_floor)

        # Calculate absolute distance
        distance = abs(current_floor - reference_floor)

        # Increase the total count
        seek_count += distance

        # Accessed floor is now new current_floor(head)
        reference_floor = current_floor

    # Once reached the up end jump to the last floor that is needed to be serviced in bottom direction
    seek_count += abs(reference_floor - down[0])
    reference_floor = down[0]

    # Now service the requests again which are bottom
    for i in range(len(down)):
        current_floor = down[i]

        # Appending current track to
        # seek sequence
        seek_sequence.append(current_floor)

        # Calculate absolute distance
        distance = abs(current_floor - reference_floor)

        # Increase the total count
        seek_count += distance

        # Accessed track is now the new head
        reference_floor = current_floor

    print("Total number of seek operations =", seek_count)
    print("Seek Sequence is")

    for i in range(len(seek_sequence)):
        print(seek_sequence[i])


# Request array
request_array = [1, 2, 3, 4, 5, 6, 7, 8]
initial_floor = 5

print("Initial position of head:", initial_floor)

c_look(request_array, initial_floor)