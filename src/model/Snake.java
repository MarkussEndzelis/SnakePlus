package model;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import engine.Direction;

public class Snake {
	private final Deque<Point> body = new ArrayDeque<>();
	private Direction direction = Direction.RIGHT;
	private Direction nextDirection = Direction.RIGHT;
	private boolean growing = false;

	public Snake(int startX, int startY) {
		body.addFirst(new Point(startX, startY));
		body.addFirst(new Point(startX + 1, startY));
		body.addFirst(new Point(startX + 2, startY));
	}

	public void setNextDirection(Direction d) {
		if (!d.isOpposite(direction)) {
			nextDirection = d;
		}
	}

	public void move() {
		direction = nextDirection;
		Point head = body.peekFirst();
		Point newHead = switch (direction) {
		case UP -> new Point(head.x, head.y - 1);
		case DOWN -> new Point(head.x, head.y + 1);
		case LEFT -> new Point(head.x - 1, head.y);
		case RIGHT -> new Point(head.x + 1, head.y);
		};

		body.addFirst(newHead);
		if (!growing) {
			body.removeLast();
		} else {
			growing = false;
		}
	}

	public void grow() {
		growing = true;
	}

	public void shrink(int amount) {
		for (int i = 0; i < amount && body.size() > 1; i++) {
			body.removeLast();
		}
	}

	public boolean collidesWithSelf() {
		Point head = body.peekFirst();
		return body.stream().skip(1).anyMatch(p -> p.equals(head));
	}

	public Set<Point> getBodySet() {
		return new HashSet<>(body);
	}

	public Deque<Point> getBody() {
		return body;
	}

	public Point getHead() {
		return body.peekFirst();
	}

	public Direction getDirection() {
		return direction;
	}

	public int getLength() {
		return body.size();
	}
}
