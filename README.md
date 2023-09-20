# Geospatial-hotspot-Analysis-

Hot Zone Analysis: I am required to find the numbers of points within the rectangle. And then calculate the hotness of all the rectangles. Basically, first, I need to get the boundaries of the rectangle. And then I need to check if the point is in the rectangle to get the Boolean. Finally, I need to count the numbers in each rectangle and sort them in an ascending order.

Hot Cell Analysis: I need to calculate the Getis-Ord statistic(G-score) of every cell. The cell here refers to the location and time, where x is the latitude of the location, y is the longitude of the location and z is the pickup date. The G-score is calculated by using the formular in Figure 1.

Figure 1 Getis-Ord statistic formular

In this formular, I need to figure out the meaning of every parameter. During this process, I need to understand how the time and space aggregated into cube cells and how to calculate the special weight and neighbors (Figure 2).
Special weight Wij:
wij = 1, if i, j are neighbors
= 0, if i, j are not neighbor

The neighbors include all cells in the range ([x-1, x+1], [y-1, y+1], [z-1, z+1]). There are four possibilities:
For cell i, which is located in the corner: ∑ wi,j = 7
For cell i, which is located in the middle of the edge: ∑ wi,j = 11
For cell i, which is located in the middle of the side: ∑ wi,j = 17
For cell i, which is located in the center: ∑ wi,j = 26

Figure 2 Space-time cube
