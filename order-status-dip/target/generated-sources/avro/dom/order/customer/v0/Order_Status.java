/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package dom.order.customer.v0;
@org.apache.avro.specific.AvroGenerated
public enum Order_Status implements org.apache.avro.generic.GenericEnumSymbol<Order_Status> {
  SUBMIT, OPEN, DOWN_TO_ROUTING, LABEL, ROUTED, SHIPPED, CANCEL  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"Order_Status\",\"namespace\":\"dom.order.customer.v0\",\"symbols\":[\"SUBMIT\",\"OPEN\",\"DOWN_TO_ROUTING\",\"LABEL\",\"ROUTED\",\"SHIPPED\",\"CANCEL\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}
