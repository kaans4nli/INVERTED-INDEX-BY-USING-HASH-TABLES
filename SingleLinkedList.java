package blessed_data_hw;

public class SingleLinkedList {
		Node head;
		public void add(Object data) {
			Node newNode = new Node(data);
			if (head == null) {
				head = newNode;
			}
			else {
				Node temp = head;
				while (temp.getLink() != null) {
					temp = temp.getLink();
				}
				temp.setLink(newNode);
			}
		}
		
		public int size() {
			int count = 0;
			if (head != null) {
				Node temp = head;
				while (temp != null) {
					temp = temp.getLink();
					count++;
				}
			}
			return count;
		}
		
		public void display() {
			if (head != null) {
				Node temp = head;
				while (temp != null) {
					System.out.println(temp.getFrequency() + "-" + temp.getData());
					temp = temp.getLink();
				}
			}
		}
		
		public Object getFreqOfValue(Object value) {
			if (head != null) {
				Node temp = head;
				while (temp != null) {
					if (temp.getData().equals(value)) {
						return temp.getFrequency();
					}
					temp = temp.getLink();
				}
			}
			return null;
		}
		
		public void containOfValue(Object value) {
			if (head != null) {
				Node temp = head;
				while (temp != null) {
					if (temp.getData().equals(value)) {
						temp.upFrequency();
						break;
					}
					temp = temp.getLink();
				}
			}
		}
		
		public boolean isContainOfValue(Object value) {
			if (head != null) {
				Node temp = head;
				while (temp != null) {
					if (temp.getData().equals(value)) {
						return true;
					}
					temp = temp.getLink();
				}
			}
			return false;
		}
		
		public class Node {
			Object data;
			int frequency;
			Node link;
			
			public Node(Object data) {
				this.data = data;
				frequency = 0;
				link = null;
			}
			
			public Object getFrequency() {
				return frequency;
			}

			public void upFrequency() {
				frequency++;
			}

			public Object getData() {
				return data;
			}

			public void setData(Object data) {
				this.data = data;
			}

			public Node getLink() {
				return link;
			}

			public void setLink(Node link) {
				this.link = link;
			}

		}//end node

		
	//end sll
	

}
