# Geospatial-hotspot-Analysis-

Hot Zone Analysis: I am required to find the numbers of points within the rectangle. And then calculate the hotness of all the rectangles. Essentially, my initial steps involve identifying the bounds of the rectangle and then assessing whether each point falls within it to obtain a Boolean value. Afterward, I must tabulate the counts for each rectangle and arrange them in ascending order.

Hot Cell Analysis: My objective is to compute the Getis-Ord statistic (referred to as G-score) for each cell. In this context, a cell is defined by its geographic coordinates, where x is the latitude of the location, y is the longitude of the location and z is the pickup date. The G-score is calculated by using the formular in Figure 1.

<img width="365" alt="image" src="https://github.com/ZSS57/Geospatial-hotspot-Analysis-/assets/101138757/d79bea6c-967a-4600-9177-f6f7eb820179">

Figure 1 Getis-Ord statistic formular

In this formular, I must decipher the significance of each parameter. Throughout this procedure, it's crucial for me to comprehend how time and space are aggregated into cube-shaped cells and how to compute the spatial weight and identify neighboring cells. (Figure 2).

        Special weight Wij:
    wij = 1, if i, j are neighbors
    = 0, if i, j are not neighbor

    The neighbors include all cells in the range ([x-1, x+1], [y-1, y+1], [z-1, z+1]). There are four possibilities:
    For cell i, which is located in the corner: ∑ wi,j = 7
    For cell i, which is located in the middle of the edge: ∑ wi,j = 11
    For cell i, which is located in the middle of the side: ∑ wi,j = 17
    For cell i, which is located in the center: ∑ wi,j = 26

<img width="435" alt="image" src="https://github.com/ZSS57/Geospatial-hotspot-Analysis-/assets/101138757/aa85c2a9-4545-411c-a860-5af4145ece60">

Figure 2 Space-time cube


