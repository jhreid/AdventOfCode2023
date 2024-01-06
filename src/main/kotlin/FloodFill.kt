import day10.Pos

fun flood_fill(pos: Pos, maze: Array<CharArray>, fill: Char, positionsInPath: List<Pos>) {
    if (pos.x == -1 || pos.y == -1 || pos.x >= maze[0].size || pos.y >= maze.size) return
    if (maze[pos.y][pos.x] == fill) return

    if (positionsInPath.contains(pos)) return

    maze[pos.y][pos.x] = fill

    flood_fill(Pos(pos.x + 1, pos.y), maze, fill, positionsInPath) // then i can either go south
    flood_fill(Pos(pos.x - 1, pos.y), maze, fill, positionsInPath) // or north
    flood_fill(Pos(pos.x, pos.y + 1), maze, fill, positionsInPath) // or east
    flood_fill(Pos(pos.x, pos.y - 1), maze, fill, positionsInPath) // or west

    return
}