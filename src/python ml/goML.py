import six.moves

from dlgo import goboard
from dlgo import gotypes
from dlgo import minimax
from dlgo.utils import print_board, print_move, point_from_coords

import socket


BOARD_SIZE = 9
PORT = 12345
IP = '127.0.0.1'
LENGTH = 1024


def capture_diff(game_state):
    black_stones = 0
    white_stones = 0
    for r in range(1, game_state.board.num_rows + 1):
        for c in range(1, game_state.board.num_cols + 1):
            p = gotypes.Point(r, c)
            color = game_state.board.get(p)
            if color == gotypes.Player.black:
                black_stones += 1
            elif color == gotypes.Player.white:
                white_stones += 1
    diff = black_stones - white_stones
    if game_state.next_player == gotypes.Player.black:
        return diff
    return -1 * diff


def send_data(sock, msg):
    sent = sock.send(msg.encode())
    if sent == 0:
        raise RuntimeError('Error. Connection Closed')


def receive_data(sock):
    bytes_recd = 0
    while bytes_recd == 0:
        try:
            chunk = sock.recv(2048)
        except:
            raise RuntimeError('Error. Connection Closed')
        if chunk == b'':
            return ''
        return chunk.decode('utf-8')


def play_over_sockets():
    print('Binding To: ' + IP + '@' + str(PORT))
    java_socket = None
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((IP, PORT))
    server_socket.listen(5) #accepting only one client.

    print('Awaiting Java...')
    (java_socket, java_ip) = server_socket.accept()
    print('Connected')
    
    game = goboard.GameState.new_game(BOARD_SIZE)
    bot = minimax.AlphaBetaAgent(1, capture_diff)

    while not game.is_over():
        print_board(game.board)
        if game.next_player == gotypes.Player.black:
            while True:
                try:
                    print('Awaiting Human Input:')
                    human_move = receive_data(java_socket)
                    print('Human Input Received:' + human_move)
                except RuntimeError:
                    return 
                if human_move != '':
                    break
                if human_move == 'VIC':
                    return
            point = point_from_coords(human_move.strip())
            move = goboard.Move.play(point)
        else:
            print('Calculating Move...')
            move = bot.select_move(game)
            print('Calculated.')
            try:
                send_data(java_socket, str(9 - move.point.row) + str(move.point.col - 1) + "\n")
            except RuntimeError:
                return
        print_move(game.next_player, move)
        game = game.apply_move(move)


def play_localy():
    game = goboard.GameState.new_game(BOARD_SIZE)
    bot = minimax.AlphaBetaAgent(1, capture_diff)

    while not game.is_over():
        print_board(game.board)
        if game.next_player == gotypes.Player.black:
            human_move = six.moves.input('-- ')
            point = point_from_coords(human_move.strip())
            move = goboard.Move.play(point)
        else:
            move = bot.select_move(game)
        print_move(game.next_player, move)
        game = game.apply_move(move)


if __name__ == '__main__':
    play_over_sockets()
