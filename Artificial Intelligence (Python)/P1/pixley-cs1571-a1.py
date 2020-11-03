import time
from math import sqrt, sin, cos, atan2, radians

import csp
import utils
import search

ex1 = '...1.13..32.2...'
ex2 = '4.2.2.3..4...24.'
ex3 = '.3..4..32.313.2.'

class CourseScheduler(csp.CSP):
    def __init__(self, courselist, num_slots):
        variables = []
        sections = []

        # parse input data assuming contents of sample file input as a single string
        classes = courselist.split('\n')
        for c in classes:
            num, name, num_sections, num_labs, num_recitations, professors, p_sections, areas = c.split(';')
            num_sections = int(num_sections)
            if areas:
                areas = areas.split(',')
                for i in range(len(areas)): areas[i] = areas[i].lstrip()
            if professors:
                professors = professors.split(',')
                for i in range(len(professors)): professors[i] = professors[i].lstrip()
                p_sections = p_sections.split(',')
                for i in range(len(p_sections)): p_sections[i] = int(p_sections[i].lstrip())
                for i in range(len(professors)):
                    for j in range(int(p_sections[i])):
                        key = str(num) + '-' + str(professors[i].split('.')[1].lstrip()) + '-' + str(num_sections)
                        sections.append({'key': key, 'num': num, 'name': name, 'section': num_sections, 'professor': professors[i], 'areas': areas})
                        num_sections -= 1
            for i in range(num_sections):
                key = str(num) + '-' + str(i)
                sections.append({'key': key, 'num': num, 'name': name, 'section': i, 'professor': None, 'areas': areas})

        # NEIGHBORS: all sections with same name, same professor, or same area (allows constraint to just be different values)
        neighbors = {s['key']: set() for s in sections}

        for s in sections:
            for sec in sections:
                if (sec['num'] == s['num']) \
                    or (s['professor'] and (sec['professor'] == s['professor'])) \
                    or (set(s['areas']).intersection(sec['areas'])):
                    if sec['key'] != s['key']: neighbors[s['key']].add(sec['key'])
            # populate variables array while we're at it
            variables.append(s['key'])

        domains = {var: range(int(num_slots))
                   for var in variables}
        csp.CSP.__init__(self, variables, domains, neighbors, csp.different_values_constraint)

def sudokuSolver(grid, algorithm):
    output = open('sudoku_output.txt', 'w')

    num_nodes = max_nodes = 0

    if len(grid) == 81:
        e = csp.Sudoku(grid)
    elif len(grid) == 16:
        e = csp.Sudoku2(grid)

    #e.display(e.infer_assignment())
    #print()

    if algorithm == 'bfs':
        start = time.time()
        node, num_nodes, max_nodes = search.breadth_first_tree_search(e)
        stop = time.time()
        solution = {v: node.state[v][1]
                    for v in range(len(node.state))}
    elif algorithm == 'dfs':
        start = time.time()
        node, num_nodes, max_nodes = search.depth_first_tree_search(e)
        stop = time.time()
        solution = {v: node.state[v][1]
                    for v in range(len(node.state))}
    elif algorithm == 'backtracking' or algorithm == 'backtracking-ordered':
        start = time.time()
        result, num_nodes = csp.backtracking_search(e, csp.mrv, csp.lcv, csp.forward_checking)
        stop = time.time()
        solution = e.infer_assignment()
    elif algorithm == 'backtracking-noOrdering':
        start = time.time()
        result, num_nodes = csp.backtracking_search(e, inference=csp.forward_checking)
        stop = time.time()
        solution = e.infer_assignment()
    elif algorithm == 'backtracking-reverse':
        start = time.time()
        result, num_nodes = csp.backtracking_search(e, csp.max_rv, csp.mcv, csp.forward_checking)
        stop = time.time()
        solution = e.infer_assignment()

    #print(algorithm + ':')
    #e.display(solution)

    runtime = stop - start

    for val in solution.values():
        output.write(str(val))
    if algorithm == 'bfs' or algorithm == 'dfs':
        output.write('\n# nodes created: ' + str(num_nodes))
        output.write('\nMax nodes in memory: ' + str(max_nodes))
    else:
        output.write('\n# assignments made: ' + str(num_nodes))
    output.write('\nRuntime: ' + str(runtime) + 's')
    output.close()

def scheduleCourses(filename, num_slots):
    input = open(filename, 'r')
    output = open('courses_output.txt', 'w')
    e = CourseScheduler(input.read(), num_slots)
    result, assignments = csp.backtracking_search(e, csp.degree, csp.lcv, csp.mac)
    out = sorted(result,
                 key=lambda r: r.split('-')[0])


    for o in out:
        #print(str(o) + ', ' + str(result[o]) + ';')
        output.write(str(o) + ', ' + str(result[o]) + ';')

def findPath(start, finish, algorithm):
    """Takes start and finish interstions along with algorithm as parameters, prints path from start to finish
    along with estimated travel time
    Estimated travel time calculated based on 3mph walking speed + 1 min for ever 10 meters climbing + 1 min for
    every 20 meters descending"""
    intersections = open('partC-intersections.txt', 'r')
    distances = open('partC-distances.txt', 'r')

    g = dict()
    locations = dict()
    altitudes = dict()

    def calc_time(a, b):
        """Input 2 intersections (states), output estimated time based on lat/lon coordinates and triginometry"""
        r = 3960.0 #radius of earth in miles
        xa, ya = locations[a]
        za = altitudes[a]
        xb, yb = locations[b]
        zb = altitudes[b]
        xa = radians(xa)
        ya = radians(ya)
        xb = radians(xb)
        yb = radians(yb)
        x_diff = xb - xa
        y_diff = yb - ya
        z_diff = zb - za
        c = sin(x_diff / 2) ** 2 + cos(xa) * cos(xb) * sin(y_diff / 2) ** 2
        d = 2 * atan2(sqrt(c), sqrt(1 - c))
        d_mile = d * r
        time = (d_mile / (3.0 / 60.0) + z_diff / 10.0) if z_diff > 0 else (d_mile / (3.0 / 60.0) - z_diff / 20.0)
        return time

    for i in intersections.read().split('\n'):
        parts = i.split(',')
        i1 = parts[0] + ',' + parts[1]
        x = float(parts[2])
        y = float(parts[3])
        z = float(parts[4])
        g.update( {i1: dict()})
        locations.update( {i1: (x, y)} )
        altitudes.update( {i1: z} )

    for d in distances.read().split('\n'):
        # Create graph with interesections as nodes and travel time (NOT distance) as travel cost
        parts = d.split(',')
        i1 = parts[0] + ',' + parts[1]
        i2 = parts[2] + ',' + parts[3]
        dist = float(parts[4])
        time = calc_time(i1, i2)
        g[i1].update({i2: time})

    oakland_map = search.UndirectedGraph(g)
    oakland_map.locations = locations

    def time_h(node):
        """ Time heuristic for Astar search (estimated time to reach goal state) """
        return calc_time(node.state, finish)

    e = search.GraphProblem(start, finish, oakland_map)

    if algorithm == 'Astar': path = search.astar_search(e, h=time_h).path()
    elif algorithm == 'idAstar': path = search.id_astar_search(e, h=time_h).path()
    output = ''
    cost = 0.0
    ascent = 0
    descent = 0
    for i in range(len(path) - 1):
        a = path[i].state
        b = path[i+1].state
        output += a + ','
        z_diff = altitudes[b] - altitudes[a]
        if z_diff > 0: ascent += z_diff
        else: descent -= z_diff
        cost = e.path_cost(cost, a, path[i+1], b)
    output += path[len(path) - 1].state + ','
    time = round(cost)
    #time = cost
    print(output + str(time))

#sudokuSolver(ex1, 'bfs')
#scheduleCourses('partB-courseList-shortened.txt', 10)
findPath('Fifth,Thackeray', "O'Hara,Thackeray", 'Astar')