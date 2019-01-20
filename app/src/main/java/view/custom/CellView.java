package view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.SupremeManufacture.DrawLines.R;

import logic.helpers.MyLogs;
import logic.helpers.ThemeColorsHelper;

public class CellView extends View {

    public enum ShapeType {
        UP,
        DOWN,
        LEFT,
        RIGHT,

        UP_DOWN,
        LEFT_RIGHT,

        UP_LEFT,
        UP_RIGHT,
        DOWN_LEFT,
        DOWN_RIGHT,

        CIRCLE_UP,
        CIRCLE_DOWN,
        CIRCLE_LEFT,
        CIRCLE_RIGHT,

        CIRCLE,
        NONE
    }

    //vars describing view
    private ShapeType shapeType;
    private int indexRow;
    private int indexCol;
    private String uniqueId;
    private int color;
    private boolean parent;
    private Paint paint;


    public CellView(Context context, ShapeType shapeType, String uniqueId, int color, int indexRow, int indexCol, boolean isParent) {
        super(context);
        this.shapeType = shapeType;
        this.uniqueId = uniqueId;
        this.color = color;
        this.indexRow = indexRow;
        this.indexCol = indexCol;
        this.parent = isParent;
        this.paint = new Paint();
    }

    public CellView(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        //Sets the cells as square
        if (width > height) {
            size = height;
        } else {
            size = width;
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(this.color);
        this.setBackgroundResource(R.drawable.cell_shape);

        // rect params
        float rectIndexLeft;
        float rectIndexTop;
        float rectIndexRight;
        float rectIndexBottom;

        // circle params
        float circleIndexCX;
        float circleIndexCY;
        float circleRadius;

        switch (this.shapeType) {
            case UP:
                MyLogs.LOG("CellView", "onDraw", "UP");
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = 0;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight() / 2;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case DOWN:
                MyLogs.LOG("CellView", "onDraw", "DOWN");
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = (float) getHeight() / 2;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight();
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case LEFT:
                MyLogs.LOG("CellView", "onDraw", "LEFT");
                rectIndexLeft = 0;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth() / 2;
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case RIGHT:
                MyLogs.LOG("CellView", "onDraw", "RIGHT");
                rectIndexLeft = (float) getWidth() / 2;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth();
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case UP_DOWN:
                MyLogs.LOG("CellView", "onDraw", "UP_DOWN");
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = 0;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight();
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case LEFT_RIGHT:
                MyLogs.LOG("CellView", "onDraw", "LEFT_RIGHT");
                rectIndexLeft = 0;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth();
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case UP_LEFT:
                MyLogs.LOG("CellView", "onDraw", "UP_LEFT");
                // Initialiser les paramètres du 1er rectangle
                rectIndexLeft = 0;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth() / 2;
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);

                // Initialiser les paramètres du 2eme rectangle
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = 0;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case UP_RIGHT:
                MyLogs.LOG("CellView", "onDraw", "UP_RIGHT");
                // Initialiser les paramètres du 1er rectangle
                rectIndexLeft = (float) getWidth() / 2;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth();
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);

                // Initialiser les paramètres du 2eme rectangle
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = 0;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case DOWN_LEFT:
                MyLogs.LOG("CellView", "onDraw", "DOWN_LEFT");
                // Initialiser les paramètres du 1er rectangle
                rectIndexLeft = 0;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth() / 2;
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);

                // Initialiser les paramètres du 2eme rectangle
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = (float) getHeight() / 3;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight();
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case DOWN_RIGHT:
                MyLogs.LOG("CellView", "onDraw", "DOWN_RIGHT");
                // Initialiser les paramètres du 1er rectangle
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth();
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);

                // Initialiser les paramètres du 2eme rectangle
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = (float) getHeight() / 2;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight();
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;


            case CIRCLE:
                MyLogs.LOG("CellView", "onDraw", "CIRCLE");
                circleIndexCX = (float) getWidth() / 2;
                circleIndexCY = (float) getHeight() / 2;
                circleRadius = (float) getHeight() / 3;
                canvas.drawCircle(circleIndexCX, circleIndexCY, circleRadius, paint);
                break;

            case CIRCLE_RIGHT:
                MyLogs.LOG("CellView", "onDraw", "CIRCLE_RIGHT");
                //circle params
                circleIndexCX = (float) getWidth() / 2;
                circleIndexCY = (float) getHeight() / 2;
                circleRadius = (float) getHeight() / 3;
                canvas.drawCircle(circleIndexCX, circleIndexCY, circleRadius, paint);

                //second rect
                rectIndexLeft = (float) getWidth() / 2;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth();
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case CIRCLE_UP:
                MyLogs.LOG("CellView", "onDraw", "CIRCLE_UP");
                //circle params
                circleIndexCX = (float) getWidth() / 2;
                circleIndexCY = (float) getHeight() / 2;
                circleRadius = (float) getHeight() / 3;
                canvas.drawCircle(circleIndexCX, circleIndexCY, circleRadius, paint);

                //second rect
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = 0;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case CIRCLE_LEFT:
                MyLogs.LOG("CellView", "onDraw", "CIRCLE_LEFT");
                //circle params
                circleIndexCX = (float) getWidth() / 2;
                circleIndexCY = (float) getHeight() / 2;
                circleRadius = (float) getHeight() / 3;
                canvas.drawCircle(circleIndexCX, circleIndexCY, circleRadius, paint);

                //second rect
                rectIndexLeft = 0;
                rectIndexTop = (float) getWidth() / 3;
                rectIndexRight = (float) getWidth() / 2;
                rectIndexBottom = (float) getHeight() * 2 / 3;
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            case CIRCLE_DOWN:
                MyLogs.LOG("CellView", "onDraw", "CIRCLE_DOWN");
                //circle params
                circleIndexCX = (float) getWidth() / 2;
                circleIndexCY = (float) getHeight() / 2;
                circleRadius = (float) getHeight() / 3;
                canvas.drawCircle(circleIndexCX, circleIndexCY, circleRadius, paint);

                //second rect
                rectIndexLeft = (float) getWidth() / 3;
                rectIndexTop = (float) getHeight() / 3;
                rectIndexRight = (float) getWidth() * 2 / 3;
                rectIndexBottom = (float) getHeight();
                canvas.drawRect(rectIndexLeft, rectIndexTop, rectIndexRight, rectIndexBottom, paint);
                break;

            default:
                MyLogs.LOG("CellView", "onDraw", "default");
                paint.setColor(ThemeColorsHelper.getBoardColor());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 3, paint);
        }
    }


    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIndexRow() {
        return indexRow;
    }

    public void setIndexRow(int indexRow) {
        this.indexRow = indexRow;
    }

    public int getIndexCol() {
        return indexCol;
    }

    public void setIndexCol(int indexCol) {
        this.indexCol = indexCol;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}