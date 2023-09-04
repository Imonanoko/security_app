class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode outcome = new ListNode();
        ListNode p1 = l1;
        ListNode p2 = l2;
        int sum;
        int carry=0;
        while (p1!=null||p2!=null){
            sum=carry;
            if (p1!=null){
                sum+= p1.val;
                p1 = p1.next;
            }
            if (p2!=null){
                sum+= p2.val;
                p1 = p2.next;
            }
            carry = sum/10;
            outcome.val = sum%10;
            outcome.next = new ListNode();
        }
        return outcome;
    }
}
class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}


