/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private int width;
    private int height;
    // private int lastIndex;
    private Color[][] colors;
    private boolean ifModified;

    // col i,row j
    private boolean onBound(int i, int j) {
        return i == 0 || i == width() - 1 || j == 0 || j == height() - 1;
    }

    private Color getColor(int col, int row) {
        if (colors[col][row] == null) {
            colors[col][row] = picture.get(col, row);
        }
        return colors[col][row];
    }

    // dual-gradient energy
    private double computeEnergy(int col, int row) {
        if (onBound(col, row))
            return 1000;
        // 首先计算deltaX
        Color left = getColor(col - 1, row);
        Color right = getColor(col + 1, row);
        double Rx = left.getRed() - right.getRed();
        double Bx = left.getBlue() - right.getBlue();
        double Gx = left.getGreen() - right.getGreen();
        double deltaX = Rx * Rx + Bx * Bx + Gx * Gx;
        // deltaY
        Color up = getColor(col, row + 1);
        Color down = getColor(col, row - 1);
        double Ry = up.getRed() - down.getRed();
        double By = up.getBlue() - down.getBlue();
        double Gy = up.getGreen() - down.getGreen();
        double deltaY = Ry * Ry + Gy * Gy + By * By;
        return Math.sqrt(deltaX + deltaY);
    }

    private void setEnergy() {
        // 因为第一个维度决定列数，所以是width
        energy = new double[width()][height()];
        // 设置边界上的像素点的能量为1000
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (onBound(i, j)) {
                    energy[i][j] = 1000;
                    colors[i][j] = getColor(i, j);
                }
                else {
                    // 这里要根据公式计算energy(getRGB有啥用?
                    energy[i][j] = computeEnergy(i, j);
                }
            }
        }
    }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("arg can not be null");
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        colors = new Color[width][height];
        ifModified = false;
        // FIXIT
        // 不一定是在这里设置好数组，可能会超过内存限制(等提交一次再看吧，不要过早优化
        setEnergy();
    }

    // current picture
    public Picture picture() {
        // 如果被修改过了，就要重建picture，并且要修改自己的picture，并且在最后，设置ifmodified为false
        if (ifModified) {
            Picture modified = new Picture(width(), height());
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    modified.set(i, j, colors[i][j]);
                }
            }
            picture = new Picture(modified);
            ifModified = false;
            return modified;
        }
        else
            return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (outOfBound(x, y))
            throw new IllegalArgumentException("out of bound");
        return energy[x][y];
    }

    // FIXIT:这里每次查找都会进行两次转置，可以优化，比如如果连续查找（水平），应该总共只需要两次转置
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // 按照提示：这里将原图片转置（实际上是转置energy矩阵），再调用findVertical,然后再把energy恢复
        // 这里首先对energy转置，并且存在一个副本里
        double[][] transposeEnergy = new double[height()][width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                transposeEnergy[j][i] = energy[i][j];
            }
        }
        // 然后交换引用
        double[][] p = transposeEnergy;
        transposeEnergy = energy;
        energy = p;
        // 除此之外，还要交换原来的width和height
        int t = width;
        width = height;
        height = t;
        int[] seam = findVerticalSeam();
        energy = transposeEnergy;
        t = width;
        width = height;
        height = t;
        return seam;
    }

    private boolean outOfBound(int col, int row) {
        return col < 0 || col >= width() || row < 0 || row >= height();
    }

    private void relax(int col, int row, int lastCol, int lastRow, double[][] distTo,
                       int[][] pathTo) {
        if (outOfBound(col, row))
            return;
        if (distTo[col][row] > distTo[lastCol][lastRow] + energy[col][row]) {
            distTo[col][row] = distTo[lastCol][lastRow] + energy[col][row];
            // pathTo本来要存的是上一个像素点的坐标，但是因为对于vertical，上一个的row一定是当前
            // row-1,所以这里只用保存上一个的col
            pathTo[col][row] = lastCol;
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // 表示到该像素点（包括该像素点)的总能量最小的路径的总能量
        double[][] distTo = new double[width()][height()];
        // 表示在'最短路径'中，该像素点的前一个像素点
        int[][] pathTo = new int[width()][height()];
        // distTo数组初始化为无穷大
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                // 这里为了把题目看作是单源的最短路径，增加一个虚拟的点，连向第一行的所有像素点，他的能量为0
                // 因此第一行的（vertical seam）最小能量和就是第一行每个像素点自己的能量
                if (j == 0) {
                    distTo[i][j] = energy[i][j];
                }
                else
                    distTo[i][j] = Double.MAX_VALUE;
            }
        }
        // 现在从第一行起，对一行中的每个元素进行relax，也就是计算正下，左下，右下的distTo和pathTo
        for (int i = 0; i < height() - 1; i++) {
            for (int j = 0; j < width(); j++) {
                relax(j, i + 1, j, i, distTo, pathTo);
                relax(j - 1, i + 1, j, i, distTo, pathTo);
                relax(j + 1, i + 1, j, i, distTo, pathTo);
            }
        }
        // 扫描最后一行，找出那个像素点which 对应的distTo最小
        int spEnd = -1;
        double minEnergy = Double.MAX_VALUE;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < minEnergy) {
                minEnergy = distTo[i][height() - 1];
                spEnd = i;
            }
        }
        // 根据最后一行元素的pathTo，倒着找到seam上所有的像素点的位置（对于vertical，就是col
        Stack<Integer> stack = new Stack<>();
        for (int i = height() - 1; i >= 0; i--) {
            stack.push(spEnd);
            spEnd = pathTo[spEnd][i];
        }
        int[] rs = new int[height()];
        int i = 0;
        for (int n : stack) {
            rs[i++] = n;
        }
        return rs;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || !isSeamValid(seam, false))
            throw new IllegalArgumentException("null arg!");
        // 这里首先对energy转置，并且存在一个副本里
        double[][] transposeEnergy = new double[height()][width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                transposeEnergy[j][i] = energy[i][j];
            }
        }
        // 然后交换引用
        double[][] p = transposeEnergy;
        transposeEnergy = energy;
        energy = p;
        // 相比findSeam,这里还要额外转置colors
        Color[][] transposeColors = new Color[height()][width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                transposeColors[j][i] = colors[i][j];
            }
        }
        // 交换引用
        Color[][] c = transposeColors;
        transposeColors = colors;
        colors = c;
        // 除此之外，还要交换原来的width和height
        int t = width;
        width = height;
        height = t;
        // 注意，转置之后，需要对seam做一个处理，根据观察发现：
        // FIXIT:还没搞清楚，下面这里突然又不需要处理了
        // 转置之前的行数，对应的转置之后的列数是从右往左数的，也就是a[i]=width-a[i]
        // for (int i = 0; i < height(); i++) {
        //     seam[i] = width() - 1 - seam[i];
        // }
        removeVerticalSeam(seam);
        t = width;
        width = height;
        height = t;
        // 注意：因为energy和colors都已经减少了一部分，不能简单的通过原来的备份的转置得到恢复
        // 而是应该把现在的经过修改的两个数组再次转置（另外，转置，不需要备份啊。
        // 上面划掉，这里之所以需要新建数组，是因为给出的图或者说矩阵不一定是n*n
        // 下面的width，height和当前数组的已经不一样了
        Color[][] finalColor = new Color[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                finalColor[i][j] = colors[j][i];
            }
        }
        colors = finalColor;
        double[][] finalEnergy = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                finalEnergy[i][j] = energy[j][i];
            }
        }
        energy = finalEnergy;
        // energy = transposeEnergy;
        // colors = transposeColors;
    }

    private void reCalculateEnergy(int col, int row) {
        if (!onBound(col - 1, row)) {
            energy[col - 1][row] = computeEnergy(col - 1, row);
        }
        if (!onBound(col + 1, row)) {
            energy[col + 1][row] = computeEnergy(col + 1, row);
        }
        if (!onBound(col, row + 1)) {
            energy[col][row + 1] = computeEnergy(col, row + 1);
        }
        if (!onBound(col, row - 1)) {
            energy[col][row - 1] = computeEnergy(col, row - 1);
        }

    }

    private boolean isSeamElemValid(int i, boolean isVertical) {
        if (isVertical) {
            if (i < 0 || i >= width())
                return false;
        }
        else {
            if (i < 0 || i >= height())
                return false;
        }
        return true;
    }

    private boolean isSeamValid(int[] seam, boolean isVertical) {
        if (isVertical) {
            if (seam.length != height())
                return false;
        }
        else {
            if (seam.length != width())
                return false;
        }
        if (!isSeamElemValid(seam[0], isVertical))
            return false;
        for (int i = 1; i < seam.length; i++) {
            if (!isSeamElemValid(seam[i], isVertical))
                return false;
            if (seam[i] == seam[i - 1] || seam[i] == seam[i - 1] + 1
                    || seam[i] == seam[i - 1] - 1) {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || !isSeamValid(seam, true))
            throw new IllegalArgumentException("null arg or not valid seam!");
        ifModified = true;
        // 首先修改colors
        for (int row = 0; row < height(); row++) {
            for (int col = seam[row] + 1; col < width(); col++) {
                colors[col - 1][row] = colors[col][row];
            }
        }
        // 修改energy数组：
        // 首先要把energy移一下
        for (int row = 0; row < height(); row++) {
            for (int col = seam[row] + 1; col < width(); col++) {
                energy[col - 1][row] = energy[col][row];
            }
        }
        // 然后修改width
        width--;
        // 需要修改seam每个元素上下左右的energy
        // FIXIT: 到底哪些需要重新计算?
        // for (int row = 0; row < height(); row++) {
        //     if (!outOfBound(seam[row], row) && !onBound(seam[row], row))
        //         reCalculateEnergy(seam[row], row);
        // }
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energy[i][j] = computeEnergy(i, j);
            }
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture("6x5.png");
        SeamCarver carver = new SeamCarver(picture);
        for(int i:carver.findHorizontalSeam())
        {
            System.out.println(i);
        }
        // System.out.println(carver.findHorizontalSeam());
        // carver.removeHorizontalSeam(carver.findHorizontalSeam());
        // carver.removeVerticalSeam(carver.findVerticalSeam());
        // carver.picture();
        // carver.removeVerticalSeam(carver.findVerticalSeam());
        // carver.picture();
        // carver.removeHorizontalSeam(carver.findHorizontalSeam());
        // carver.removeVerticalSeam(carver.findVerticalSeam());
        // carver.findHorizontalSeam();
    }
}
